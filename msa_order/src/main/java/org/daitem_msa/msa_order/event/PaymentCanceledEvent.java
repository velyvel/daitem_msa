package org.daitem_msa.msa_order.event;

import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.springframework.context.ApplicationEvent;

public class PaymentCanceledEvent extends ApplicationEvent {
    public PaymentCanceledEvent(Object source) {
        super(source);
    }

//    public Sting canceledOrder() {
//        return (Long) source;
//    }
}
