package org.daitem_msa.msa_order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ORDER_STATUS = "order_status";

    public void addTask(Object task) {
        // 작업을 이벤트 브로커에 발행하여 대기열에 추가
        redisTemplate.convertAndSend(ORDER_STATUS, task);
    }

    public void processTask(Object task) {
        // 대기열에서 작업을 가져와 처리
        // 작업 처리 로직
    }
}
