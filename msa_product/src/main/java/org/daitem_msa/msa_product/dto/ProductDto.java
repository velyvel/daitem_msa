package org.daitem_msa.msa_product.dto;

import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_product.enumset.Categories;

import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long productId;
    private String productName;
    @Convert(converter = Categories.CategoriesConverter.class)
    private Categories productCategory;
    private String description;

    private List<ProductDetailDto> productDetails;
}

