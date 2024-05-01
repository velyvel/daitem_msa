package org.daitem_msa.msa_order.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.common.ProductDetailFeignClient;
import org.daitem_msa.msa_order.dto.OrderListDto;
import org.daitem_msa.msa_order.dto.OrderSaveDto;
import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.entity.OrderDetail;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.enumset.YN;
import org.daitem_msa.msa_order.repository.OrderDetailRepository;
import org.daitem_msa.msa_order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductDetailFeignClient productDetailFeignClient;

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;


    @Transactional
    public void orderAdd(OrderSaveDto dto) {

        if (dto.getTotalAmount() == 0) {
            throw new RuntimeException("상품은 한 개 이상 주문하셔야 합니다");
        }

        Order order = Order.builder()
                .userId(dto.getUserId())
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDERED)
                .paymentMethod(dto.getPaymentMethod())
                .shippingAddress1(dto.getShippingAddress1())
                .shippingAddress2(dto.getShippingAddress2())
                .shippingAddress3(dto.getShippingAddress3())
                .totalAmount(dto.getTotalAmount())
                .isDelivered(YN.N)
                .build();
        orderRepository.save(order);

        // 재고 빼기
        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < dto.getProductDetails().size(); i++) {
            Long productDetailId = dto.getProductDetails().get(i).getProductDetailId();
            int stock = dto.getProductDetails().get(i).getAmount();
            map.put(productDetailId,stock);
        }
        productDetailFeignClient.updateStocksOrder(map);

        //orderDetail save
        orderDetailService.addOrderDetails(dto, order);
        }

    @Transactional
    public List<OrderListDto> orderList(Long userId) {
    return orderRepository.findAllByUserId(userId);
    }

    @Transactional
    public void orderCanceled(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문정보를 찾을 수 없습니다."));

        if (order.getOrderDate().plusDays(1L).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("주문 취소 기간이 지났습니다.");
        }
        // 주문 상세 조회하기
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);

        //재고 돌려놓기
        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < orderDetails.size(); i++) {
            Long productDetailId = orderDetails.get(i).getProductDetailId();
            int quantity = orderDetails.get(i).getQuantity();
            map.put(productDetailId,quantity);
        }
        productDetailFeignClient.updateStocksReturn(map);

        //주문에 띄워주기
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

}


