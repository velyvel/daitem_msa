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
public class OrderSaveDto {
    private Long userId;
    private LocalDateTime orderDate;
    private int totalAmount;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;

    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingAddress3;
    private YN isDelivered;
//    //주문 상세 리스트
//    private List<OrderProductDto> products;
    /**여기에 상품 아이디를 넣어 한번에 가져오자*/
    private List<OrderProductDetailDto> productDetails;
}
