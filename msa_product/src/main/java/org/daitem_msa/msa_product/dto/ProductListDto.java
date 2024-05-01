package org.daitem_msa.msa_product.dto;

import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_product.enumset.Categories;


@Getter
@Setter
public class ProductListDto {

    private Long productId;
    private String productName;
    @Convert(converter = Categories.CategoriesConverter.class)
    private Categories productCategory;
//    @Convert(converter = ColorConverter.class)
//    private Colors color;
    private String colors;
}
