package org.daitem_msa.msa_product.repository;

import org.daitem_msa.msa_product.entity.Product;
import org.daitem_msa.msa_product.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ProductDetailRepository extends
        JpaRepository<ProductDetail, Long>,
        QuerydslPredicateExecutor<ProductDetail>,
        ProductDetailRepositoryCustom {
    List<ProductDetail> findByProduct(Product product);
}
