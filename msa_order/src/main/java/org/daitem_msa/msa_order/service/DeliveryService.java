package org.daitem_msa.msa_order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.enumset.YN;
import org.daitem_msa.msa_order.repository.OrderDetailRepository;
import org.daitem_msa.msa_order.repository.OrderRepository;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Transactional
    public void CanceledDelivery(User user, Long id) {
        if(user == null) {
          throw new RuntimeException("회원정보가 올바르지 않습니다.");
        }
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("올바르지 않은 주문 정보입니다"));
        // 반품조건 만족하는 메서드
        canDelivered(order);
    }

    private void canDelivered(Order order) {

        //배송 완료, 배송 상태값 delivered, 2024.04.26 00 : 01 배송 완료
        //하루의 개념 ?
        if(order.getIsDelivered().equals(YN.Y)
                && order.getOrderStatus().equals(OrderStatus.DELIVERED)
                && order.getDeliveryDate().plusDays(1L).isBefore(LocalDateTime.now())){
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("반품기간이 지났습니다.");
        }
    }
}
