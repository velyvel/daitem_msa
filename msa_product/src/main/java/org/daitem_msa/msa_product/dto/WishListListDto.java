package org.daitem_msa.msa_product.dto;

import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_product.enumset.Categories;
import org.daitem_msa.msa_product.enumset.Colors;
import org.daitem_msa.msa_product.enumset.DisplayType;
import org.daitem_msa.msa_product.enumset.Size;

@Getter
@Setter
public class WishListListDto {
    private Long productId;
    private Long userId;
    private String productName;
    private String description;
    private Categories productCategory;
    private Long productDetailId;
    private Colors color;
    private Size size;
    private int price;
    private DisplayType displayType;
    private int count;

}
