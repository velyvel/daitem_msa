package org.daitem_msa.msa_product.dto;

import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_product.enumset.Categories;

import java.util.List;


@Getter
@Setter
public class ProductUpdateDto {

    private String productName;
    private String description;
    private Categories categories;
    private List<ProductDetailUpdateDto> productDetails;
//    private int price;
//    private int stock;
//    private YN isSalesTerms;
//    private LocalDate saleFromDate;
//    private LocalDate saleToDate;
//
//    private DisplayType displayType;
//    private YN isDeleted;
//    private Colors color;

}
