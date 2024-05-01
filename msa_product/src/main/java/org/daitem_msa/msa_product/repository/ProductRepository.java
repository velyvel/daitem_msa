package org.daitem_msa.msa_product.repository;

import org.daitem_msa.msa_product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends
        JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>,
        ProductRepositoryCustom {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productDetails pd WHERE p.productId = :productId AND pd.productDetailId = :productDetailId")
    Optional<Product> findByProductIdAndProductDetailId(@Param("productId") Long productId, @Param("productDetailId") Long productDetailId);
}
