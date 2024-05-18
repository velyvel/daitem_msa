package org.daitem_msa.msa_order.repository;


import jakarta.persistence.LockModeType;
import org.daitem_msa.msa_order.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemsRepository extends
        JpaRepository<Items, Long>,
        QuerydslPredicateExecutor<Items>{

    Optional<Items> findByUserId(Long userId);

    Optional<Items> findByProductDetailIdAndUserId(Long productDetailId, Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Items i where i.itemId = :itemId")
    Optional<Items> findById2(@Param("itemId") Long itemId);
}
