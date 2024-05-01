package org.daitem_msa.msa_order.repository;


import org.daitem_msa.msa_order.dto.OrderListDto;
import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface OrderRepository extends
        JpaRepository<Order, Long>,
        QuerydslPredicateExecutor<Order>,
        OrderRepositoryCustom{

    List<OrderListDto> findAllByUserId(Long userId);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
