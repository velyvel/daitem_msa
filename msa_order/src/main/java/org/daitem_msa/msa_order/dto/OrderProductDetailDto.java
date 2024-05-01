package org.daitem_msa.msa_order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDetailDto {
    private Long productId;
    private Long productDetailId;
    private int amount;
}
