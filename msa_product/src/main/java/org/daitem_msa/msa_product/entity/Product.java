package org.daitem_msa.msa_product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.daitem_msa.msa_product.enumset.Categories;
import org.daitem_msa.msa_user.enumset.YN;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;

    @Convert(converter = Categories.CategoriesConverter.class)
    private Categories productCategory;

    private String description;

    private YN isRealDelete;

    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetails;

//    @OneToMany(mappedBy = "product")
//    private List<OrderDetail> orderDetails;

//size, color 은 참조만
}