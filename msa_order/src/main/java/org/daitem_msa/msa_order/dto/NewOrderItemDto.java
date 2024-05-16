package org.daitem_msa.msa_order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewOrderItemDto {
    private Long itemId;
    private int stock;
    private Long productDetailId;
    //private Long productDetailId;
}
