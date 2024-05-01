package org.daitem_msa.msa_order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailListDto {
    private Long productId;

    private String productName;
    private String description;
    //detail
    private Long productDetailId;
    private String color;
    private String size;
    private String displayType;

    private int price;
    private Long orderId;
    private int quantity;
    private int productPrice;
    // quantity * price
    // private int totalPrice;
    //private int totalSumPrice;

}
