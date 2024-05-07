package org.daitem_msa.msa_order.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.common.redisson.DistributionLock;
import org.daitem_msa.msa_order.common.ProductDetailFeignClient;
import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.daitem_msa.msa_order.entity.Items;
import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.entity.OrderDetail;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.enumset.PaymentMethod;
import org.daitem_msa.msa_order.enumset.YN;
import org.daitem_msa.msa_order.repository.ItemsRepository;
import org.daitem_msa.msa_order.repository.OrderDetailRepository;
import org.daitem_msa.msa_order.repository.OrderRepository;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewOrderService {
    /***
     * EDM : Event Driven 방식으로 처리할 수 있도록, 예약 구매
     * 회복탄력성 : resilience4j의 circuit breaker 와 retry 사용 : retry 횟수 (사용자)
     */


    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final ProductDetailFeignClient productDetailFeignClient;
    private final ItemsRepository itemsRepository;

    private final RedisTemplate<String, Object> redisTemplate;
    //private final KafkaTemplate<String, Object> kafkaTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> listOps;

    @Transactional
    public void newOrderAdd(NewOrderSaveDto dto) {

        Items items = itemsRepository.findById(1L).orElse(null);// 주문 수량 가져오기
        // 레디스 queue 에 들어온 요청(사용자, 수량)을 담고 저장해두기, poll 한 사용자 순서대로 가지고오기
        listOps.leftPush("orderQueue", dto);

        // 재고 수정 :
        if(items.getStock() > 0) {
            NewOrderSaveDto orderDto = (NewOrderSaveDto) listOps.rightPop("orderQueue");
            items.setStock(items.getStock() - 1);
            itemsRepository.save(items);

            // 주문서 생성
            Order order = Order.builder()
                    .userId(orderDto.getUserId())
                    .orderDate(LocalDateTime.now())
                    .orderStatus(OrderStatus.ORDER_START)
                    .shippingAddress1("서울시")
                    .shippingAddress2("은평구")
                    .shippingAddress3("구산동")
                    .paymentMethod(PaymentMethod.NONE)
                    .totalAmount(1)
                    .isDelivered(YN.N)
                    .build();
            orderRepository.save(order);
            // order Detail 도 저장
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productDetailId(12L)
                    .productId(4L)
                    .quantity(1)
                    .build();
            orderDetailRepository.save(orderDetail);
            System.out.println("주문 처리 완료 - 주문번호: " + order.getOrderId());
            // 결제 서비스로 보냄
            doPayment(order);
        }
    }

    private void doPayment(Order order) {
        //이벤트 브로커 메세지(결제 완료)
        // 결제실패 :  set 재고 + 1

    }

    public void orderRequest(User user, Long id) {
        // 이제 items 가 아니라 상품정보를 레디스에 저장
        // 레디스에 주문자 상품 저장
        redisTemplate.opsForValue().set("userId", user.getUserId());
        // 레디스에 상품 정보 꺼내기
        String TOPIC = "orderRequest";
        Order order = Order.builder()
                .userId(user.getUserId())
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDER_START)
                .shippingAddress1("서울시")
                .shippingAddress2("은평구")
                .shippingAddress3("구산동")
                .paymentMethod(PaymentMethod.NONE)
                .totalAmount(1)
                .isDelivered(YN.N)
                .build();
        //kafkaTemplate.send(TOPIC, order);
        System.out.println("카프카 요청 완료");
        // 카프카에 주문 요청

    }
}

