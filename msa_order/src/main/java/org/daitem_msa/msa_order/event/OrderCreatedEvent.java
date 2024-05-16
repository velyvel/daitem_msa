package org.daitem_msa.msa_order.event;

import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.daitem_msa.msa_order.entity.Items;
import org.springframework.context.ApplicationEvent;

public class OrderCreatedEvent extends ApplicationEvent {
    //이벤트가 일어났을 때
    private final Items items;
    public OrderCreatedEvent(Object source, Items items) {
        super(source);
        this.items = items;
    }

    public NewOrderSaveDto getOrderDto() {
        return (NewOrderSaveDto) source;
    }

    public Items getItems() {
        return items;
    }
}
