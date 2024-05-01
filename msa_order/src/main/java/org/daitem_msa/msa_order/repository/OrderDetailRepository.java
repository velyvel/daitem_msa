package org.daitem_msa.msa_order.repository;


import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface OrderDetailRepository extends
        JpaRepository<OrderDetail, Long>,
        QuerydslPredicateExecutor<OrderDetail>,
        OrderDetailRepositoryCustom{

    List<OrderDetail> findByOrder(Order order);
}
