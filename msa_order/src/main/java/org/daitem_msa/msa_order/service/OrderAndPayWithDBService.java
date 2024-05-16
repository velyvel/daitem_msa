package org.daitem_msa.msa_order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.common.redisson.DistributionLock;
import org.daitem_msa.msa_order.dto.NewOrderItemDto;
import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.daitem_msa.msa_order.dto.NewOrderSaveOneDto;
import org.daitem_msa.msa_order.entity.Items;
import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.event.OrderCreatedEvent;
import org.daitem_msa.msa_order.event.PaymentCanceledEvent;
import org.daitem_msa.msa_order.repository.ItemsRepository;
import org.daitem_msa.msa_order.repository.OrderRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderAndPayWithDBService {
    /***
     * 동시성 처리 : 분산락 걸어서 DB에 추가
     * 이벤트 발생 :
     *  - 주문 생성 : 주문한 사용자를 큐에 추가하기, 히스토리 테이블에 저장 <- retry 를 코드레벨로 어떻게 구현하는지 어렵다
     *  - 결제 취소 : (변심)으로 탈주
     *  - 결제 완료 : 실제 재고 차감
     *  이후 캐시로 바꿀 때: Items 를 다 캐시로 바꾸면 된다, 저장은 레디스에!!
     */
    //TODO : orderDetail에도 저장하기
    private final RedissonClient redissonClient;
    private final ItemsRepository itemsRepository;
    private final Queue<Long> queue = new ArrayDeque<>();
    private final Queue<Long> orderWaitingQueue = new ArrayDeque<>();
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;


//=============================== DB에 직접 넣는 방식 ===============================//
    //상품 저장
    public void productAdd(NewOrderItemDto dto) {
        Items items = Items.builder()
                .productDetailId(dto.getProductDetailId())
                .stock(dto.getStock())
                .build();
        itemsRepository.save(items);
    }

    //주문 처리
    @DistributionLock(lockName = "orderRequest")
    public void orderRequest(Long itemId, int stock, NewOrderSaveDto dto) {
        RLock lock = redissonClient.getLock("orderRequestLock");
        try {
            // 락 획득
            lock.lock();
            log.info("============= 락을 획득하였습니다 ==========" + dto.getUserId());
            // 재고 처리 로직
            Items items = itemsRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("상품이 발견되지 않았습니다."));
            //재고가 있을 때 : 주문 생성 이벤트 발행
            if (items.getStock() > 0) {
                OrderCreatedEvent orderEvent = new OrderCreatedEvent(dto, items);
                eventPublisher.publishEvent(orderEvent);
                //주문 생성까지 완료했음 -> 결제로 보내기
            } else {
                // 재고가 없는 경우 처리
                orderWaitingQueue.add(dto.getUserId());
                throw new RuntimeException("재고가 없어서 요청을 보내지 않습니다.");
            }
        } finally {
            // 락 해제
            lock.unlock();
            log.info("=====락 해제되었습니다 =======");
        }
    }

    // 이벤트 처리
    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        NewOrderSaveDto dto = event.getOrderDto();
        Items items = event.getItems();
        // 주문 큐에 추가
        items.setStock(items.getStock() - 1);
        itemsRepository.save(items);

        // 재고 변경 내역 저장
        Items history = Items.builder()
                .productDetailId(13L)
                .stock(items.getStock())
                .userId(dto.getUserId())
                .build();
        itemsRepository.save(history);
        System.out.println("========== 재고 반영 완료 ========");
        // 결제 대기열에 큐 추가
        queue.add(dto.getUserId());
    }
    // 아 여기서 분기처리 한번 더
    public void orderPay(NewOrderSaveOneDto dto) {
        Long userId = queue.poll();
        if(userId == null) {
            //대기열에 있는지 찾아봅니다
        }
        System.out.println("결제를 하러 들어온 사용자" + userId);

        Random random = new Random();
        // 20%의 확률로 주문 취소 여부 결정
        if (random.nextDouble() <= 0.2) {
            //userId = null 인것 무조건 하나!
            Items items = itemsRepository.findByProductDetailIdAndUserId
                    (dto.getProductDetailId(), null).orElse(null);
            // 결제 취소가 되는 경우 취소 이벤트 발생, 주문 큐에 있는 순서대로
            System.out.println("주문 취소 발생 취소 발생");
            PaymentCanceledEvent paymentCanceledEvent = new PaymentCanceledEvent("주문 취소 발생 취소 발생");
            eventPublisher.publishEvent(paymentCanceledEvent);
            //재고 + 주문에 새로운 사용자 넣어주기
            items.setStock(items.getStock() + 1);
            itemsRepository.save(items);
        }
        else {
            // 실제로 주문 테이블에 넣기
            Order order = Order.builder()
                    .paymentMethod(dto.getPaymentMethod())
                    .orderDate(LocalDateTime.now())
                    .orderStatus(OrderStatus.ORDERED)
                    .shippingAddress1(dto.getShippingAddress1())
                    .shippingAddress2(dto.getShippingAddress2())
                    .shippingAddress3(dto.getShippingAddress3())
                    .totalAmount(dto.getTotalAmount())
                    .userId(userId)
                    .build();
            //orderDetail 도 저장하기
            orderRepository.save(order);
            System.out.println("========== 주문 결제 완료 ========");
        }
    }

    @EventListener
    public void handlePayCanceledEvent(PaymentCanceledEvent event) {
        // 주문 큐에 새로운 사용자 추가 : 이벤트가 메세지 역할을 한다
        if(event != null) {
            // 주문 큐에 대기열 1번 poll 해서 주문 큐에 넣어준다
            queue.add(orderWaitingQueue.poll());
        }
    }

    public int showStocks(Long id) {
        Items items = itemsRepository.findById(id).orElse(null);
        int stock = items.getStock();
        return stock;
    }
}

