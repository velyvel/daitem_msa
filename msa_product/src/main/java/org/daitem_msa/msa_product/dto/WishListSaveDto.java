package org.daitem_msa.msa_product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishListSaveDto {
    private Long wishId;
    private Long productId;
    private Long userId;
    private Long productDetailId;
}
