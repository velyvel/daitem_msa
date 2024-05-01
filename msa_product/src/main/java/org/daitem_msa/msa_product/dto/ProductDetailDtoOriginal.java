package org.daitem_msa.msa_product.dto;

import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_product.enumset.Colors;
import org.daitem_msa.msa_product.enumset.DisplayType;
import org.daitem_msa.msa_product.enumset.Size;
import org.daitem_msa.msa_user.enumset.YN;


@Getter
@Setter
public class ProductDetailDtoOriginal {

        private Long productId;
        private Long productDetailId;

        @Convert(converter = Colors.ColorsConverter.class)
        private Colors color;
        private Size size;
        private int stock;

//        private YN isSaleTerms;
//        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//        private LocalDateTime saleFromDate;
//        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//        private LocalDateTime saleToDate;

        private int price;
        private DisplayType displayType;
        private YN isDeleted;
}

