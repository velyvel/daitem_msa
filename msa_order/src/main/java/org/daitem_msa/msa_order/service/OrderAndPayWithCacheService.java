package org.daitem_msa.msa_order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.common.CommonResponse;
import org.daitem_msa.msa_order.common.redis.RedisPubSubService;
import org.daitem_msa.msa_order.common.redis.dto.MessageDto;
import org.daitem_msa.msa_order.common.redisson.DistributionLock;
import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.daitem_msa.msa_order.dto.NewOrderSaveOneDto;
import org.daitem_msa.msa_order.entity.Item;
import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.repository.OrderRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderAndPayWithCacheService {
    /***
     * EDM : Event Driven 방식으로 처리할 수 있도록, 예약 구매
     * 회복탄력성 : resilience4j의 circuit breaker 와 retry 사용 : retry 횟수 (사용자)
     */
    private final RedisTemplate<String, Item> redisTemplate;
    //주문 리스트 확인용(히스토리) -> 바로 저장(key: createOrder, value : userId)
    private final RedisTemplate<String, Map<Long,Integer>> createOrderRedisTemplate;
    //주문 생성 대기열 리스트(결제로 넘어갈 때 구현하기)
    private final RedisTemplate<String, Map<Long,Integer>> waitingOrderRedisTemplate;
    // 레디스 이벤트 발생
    private final RedisPubSubService redisPubSubService;
    private final OrderRepository orderRepository;
    Map<Long, Integer> orderInfo = new HashMap<>();
    Map<Long, Integer> waitingInfo = new HashMap<>();


    @Transactional
    public void redisProductAdd(Item items) {
        // 여기 로직은 마지막에 수정하기
        try{
            redisTemplate.opsForValue().set(items.getItemId(), items, 1, TimeUnit.DAYS);
            System.out.println("redis 에 상품 저장(하루)");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("상품 저장 실패");
        }
    }

    @Transactional
    public Item redisProductView(String itemId) {
        Item item = redisTemplate.opsForValue().get(itemId);
        if(item == null) {
            throw new RuntimeException("조회된 상품이 없습니다");
        }
        return item;
    }

    @DistributionLock(lockName = "orderRequestWithRedis")
    public void orderRequest(NewOrderSaveDto dto) {
        // 주문 이벤트 만들기
        MessageDto messageDto = new MessageDto();
        //
        redisPubSubService.makeOrderMessage(messageDto,dto);
        redisPubSubService.sendMessage("makeOrder",messageDto);
        Item item = redisTemplate.opsForValue().get(dto.getItemId());
        System.out.println(item);
        if (item.getStock() > 0) {
            item.setStock(item.getStock() - dto.getStock());
            // 재고 차감 후 저장
            redisProductAdd(item);
            // (이벤트 발생) 레디스주문(큐)에 넣어주기
            orderInfo.put(dto.getUserId(), dto.getStock());
            // 여기에 재고 + 시간 items 에 update 처리하기(실제 DB write), 상품에 대한 정보가 필요하다
            createOrderRedisTemplate.opsForList().leftPush("orderQueue", orderInfo);
            System.out.println("========== 재고 반영 완료 ========");
        } else {
            System.out.println("재고가 없어서 요청을 보내지 않습니다.");
            //주문 대기 이벤트 발생 -> 아직 구현 전
            //주문 대기 큐에 넣어주기
            waitingInfo.put(dto.getUserId(), dto.getStock());
            waitingOrderRedisTemplate.opsForList().leftPush("orderWaitingQueue", waitingInfo);
        }
    }

    // 주문 이벤트 만들기
//    private void makeOrderEvent(MessageDto messageDto, Long userId) {
//        String channel = "ddd";
//        messageDto.setMessage("주문 생성 완료");
//        messageDto.setSender("orderServer");
//        messageDto.setRecipient(userId.toString());
//    }

    // 레디스 주문 큐에 넣어준 데이터들 리스트 조회하기: queueName = 주문 큐
    public List<Map<Long, Integer>> resultWithRedisson(String itemId) {
        return createOrderRedisTemplate.opsForList().range(itemId, 0, -1);
    }


    public void orderPay(NewOrderSaveOneDto dto) {
        String queueName = "orderQueue";
        Item item = redisTemplate.opsForValue().get(dto.getItemId());
        if(item == null) {
            throw new RuntimeException("아이템을 조회할 수 없습니다.");
        }
        // 아 이게 휘발될 수 있구나.. timeout 을 줘야겠다
        Map<Long, Integer> orderPop =  createOrderRedisTemplate.opsForList().leftPop(queueName);
        Long userId = orderPop.keySet().iterator().next().longValue();
        if (orderPop == null) {
            // 대기열에 있는지 찾아보기
        }
        Random random = new Random();
        if (random.nextDouble() <= 0.2) {
            // 주문 취소 이벤트 발생(일단 큐로 구현 후 이벤트로 바꿀 예정)
            // 아이템에 재고 바꾸기
            item.setStock(item.getStock() + orderPop.get(userId));
            redisProductAdd(item);
            waitingInfo = waitingOrderRedisTemplate.opsForList().leftPop("orderWaitingQueue");
            createOrderRedisTemplate.opsForList().leftPush("orderWaitingQueue", waitingInfo);
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

    public int showStock(String id) {
        Item item = redisTemplate.opsForValue().get(id);
        return item == null ? 0 : item.getStock();
    }
}

