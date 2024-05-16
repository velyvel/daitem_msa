package org.daitem_msa.msa_order.event;

import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventListener {

//    @EventListener
//    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
//        NewOrderSaveDto orderDto = event.getOrderDto();
//        // 아 이거 아니지
//        orderDto.getUserId();
//        // 이벤트 처리 로직 작성
//        System.out.println("주문 생성 완료");
//    }
}
