package org.daitem_msa.msa_product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.daitem_msa.msa_product.enumset.Colors;
import org.daitem_msa.msa_product.enumset.DisplayType;
import org.daitem_msa.msa_product.enumset.Size;
import org.daitem_msa.msa_user.enumset.YN;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Data
@Table(name = "product_detail")
public class ProductDetail {

    //상품 디테일 시퀀스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Long productDetailId;

    // 상품 시퀀스
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Convert(converter = Colors.ColorsConverter.class)
    private Colors color;
    @Convert(converter = Size.SizeConverter.class)
    private Size size;

    private YN isSaleTerms;
    private LocalDateTime saleFromDate;
    private LocalDateTime saleToDate;
    private int stock;
    private int price;

    // 진열여부
    @Convert(converter = DisplayType.DisplayConverter.class)
    private DisplayType displayType;

    //삭제여부 : 기본값 N, 진열여부가 delete 가 되면 바뀜
    @Convert(converter = YN.YNConverter.class)
    private YN isDeleted;

}
