package org.daitem_msa.msa_order.service;

import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.ProductDetailFeignClient;
import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.entity.OrderDetail;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.enumset.YN;
import org.daitem_msa.msa_order.repository.OrderDetailRepository;
import org.daitem_msa.msa_order.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DeliveryScheduler {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailFeignClient productDetailFeignClient;

    // 매일 오전 12:01
    @Scheduled(cron = "1 0 0 * * ?")
    // 테스트 : 1분마다
    //@Scheduled(cron = "1 * * * * ?")
    public void myScheduledMethod() {

        //CANCEL 이면 ? 하루 뒤 재고 돌려놓기
        List<Order> canceledOrders = orderRepository.findByOrderStatus(OrderStatus.CANCELLED);
        for(Order order : canceledOrders) {
            LocalDate deliveredDate = order.getDeliveryDate().toLocalDate();
            if(deliveredDate.plusDays(1L).isEqual(LocalDate.now())
                    && !(order.getOrderStatus().equals(OrderStatus.CANCELLED))){

                    List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
                    // 재고 돌려놓기 재활용
                    Map<Long, Integer> map = new HashMap<>();
                    for (int i = 0; i < orderDetails.size(); i++) {
                        Long productDetailId = orderDetails.get(i).getProductDetailId();
                        int quantity = orderDetails.get(i).getQuantity();
                        map.put(productDetailId,quantity);
                    }
                    productDetailFeignClient.updateStocksReturn(map);
            }
        }

        //DELIVERED -> DETERMINE 으로 바꾸기
        List<Order> determineOrders = orderRepository.findByOrderStatus(OrderStatus.DELIVERING);
        for (Order order : determineOrders) {
            LocalDate deliveredDate = order.getDeliveryDate().toLocalDate();

            // 주문 맞으면 ?
            if (deliveredDate.plusDays(1L).isEqual(LocalDate.now())
                    && !(order.getOrderStatus().equals(OrderStatus.CANCELLED))) {
                order.setOrderStatus(OrderStatus.DETERMINE);
                orderRepository.save(order);
            }
        }

        //DELIVERING -> DELIVERED 로 바꾸기
        List<Order> orderedDelivering = orderRepository.findByOrderStatus(OrderStatus.DELIVERING);

        for (Order order : orderedDelivering) {
            LocalDate orderDate = order.getOrderDate().toLocalDate();
            if (orderDate.plusDays(2L).isEqual(LocalDate.now())) {
                order.setOrderStatus(OrderStatus.DELIVERED);
                order.setIsDelivered(YN.Y);
                order.setDeliveryDate(LocalDateTime.now());
                orderRepository.save(order);
            }
        }

        // ORDERED -> DELIVERING
        List<Order> orderedOrders = orderRepository.findByOrderStatus(OrderStatus.ORDERED);
        for (Order order : orderedOrders) {
            LocalDate orderDate = order.getOrderDate().toLocalDate();

            if (orderDate.plusDays(1L).isEqual(LocalDate.now())) {
                order.setOrderStatus(OrderStatus.DELIVERING);
                orderRepository.save(order);
            }
        }

        System.out.println("============ 배송상태 변경 완료 ==========");
    }
}
