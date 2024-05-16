package org.daitem_msa.msa_order.repository;


import org.daitem_msa.msa_order.entity.ItemHistory;
import org.daitem_msa.msa_order.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ItemsHistoryRepository extends
        JpaRepository<ItemHistory, Long>,
        QuerydslPredicateExecutor<ItemHistory>{

}
