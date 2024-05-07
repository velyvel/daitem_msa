package org.daitem_msa.msa_order.dto;

import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.enumset.PaymentMethod;
import org.daitem_msa.msa_order.enumset.YN;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NewOrderSaveDto {
    private Long userId;
    private int stock;
    //private Long productDetailId;
}
