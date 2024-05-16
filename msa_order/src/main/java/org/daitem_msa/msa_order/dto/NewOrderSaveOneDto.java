package org.daitem_msa.msa_order.dto;

import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.enumset.PaymentMethod;
import org.daitem_msa.msa_order.enumset.YN;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewOrderSaveOneDto {
    private String itemId;
    private Long userId;
    private LocalDateTime orderDate;
    private int totalAmount;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;

    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingAddress3;
    private YN isDelivered;

    private Long productId;
    private Long productDetailId;
    private int amount;
}
