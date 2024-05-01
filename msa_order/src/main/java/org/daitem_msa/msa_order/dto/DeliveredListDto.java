package org.daitem_msa.msa_order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveredListDto {
    private Long orderId;
    private Long userId;
    private Long productId;

}
