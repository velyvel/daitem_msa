package org.daitem_msa.msa_product.repository;

import org.daitem_msa.msa_product.entity.ProductDetail;
import org.daitem_msa.msa_product.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface WishListRepository extends
        JpaRepository<WishList, Long>,
        QuerydslPredicateExecutor<WishList>,
        WishListRepositoryCustom{

    WishList findByProductDetail(ProductDetail productDetail);

    WishList findByUserIdAndProductDetail(Long userId, ProductDetail productDetail);

    List<WishList> findAllByUserId(Long userId);
//
//    List<WishList> findByUser(User user);
}
