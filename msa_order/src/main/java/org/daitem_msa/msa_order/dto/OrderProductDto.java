package org.daitem_msa.msa_order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class OrderProductDto {
    private Long productId;
    private List<OrderProductDetailDto> productDetails;
}
