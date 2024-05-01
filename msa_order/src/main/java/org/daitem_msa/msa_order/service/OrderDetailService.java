package org.daitem_msa.msa_order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.common.ProductDetailFeignClient;
import org.daitem_msa.msa_order.dto.OrderDetailListDto;
import org.daitem_msa.msa_order.dto.OrderProductDetailDto;
import org.daitem_msa.msa_order.dto.OrderSaveDto;
import org.daitem_msa.msa_order.entity.Order;
import org.daitem_msa.msa_order.entity.OrderDetail;
import org.daitem_msa.msa_order.repository.OrderDetailRepository;
import org.daitem_msa.msa_order.repository.OrderRepository;
import org.daitem_msa.msa_product.dto.ProductDetailDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductDetailFeignClient productDetailFeignClient;

    public void addOrderDetails(OrderSaveDto dto, Order order) {

        for (OrderProductDetailDto productDetailDto : dto.getProductDetails()) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productDetailId(productDetailDto.getProductDetailId())
                    .productId(productDetailDto.getProductId())
                    .quantity(productDetailDto.getAmount())
                    .build();
            orderDetailRepository.save(orderDetail);
        }
    }

    // 주문 상세정보 조회하기
    // 서비스 로직
    @Transactional
    public List<OrderDetailListDto> getOrderDetails(Long id) {

        // 주문 정보 조회
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문정보를 찾을 수 없습니다"));

        List<OrderDetailListDto> orderDetailListDto = new ArrayList<>();

        // 주문 상세 정보 조회
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
        for (OrderDetail orderDetail : orderDetails) {
            // 상품 상세 정보 조회
            //ProductDetail productDetail = productDetailFeignClient.findById(orderDetail.getProductDetailId());

            ProductDetailDto productDetail = productDetailFeignClient.findById(orderDetail.getProductDetailId());
            // 주문 상세 목록 DTO 생성
            OrderDetailListDto dto = new OrderDetailListDto();
            dto.setProductId(productDetail.getProductId());
            dto.setProductName(productDetail.getProductName());
            dto.setDescription(productDetail.getDescription());
            dto.setProductDetailId(productDetail.getProductDetailId());
            dto.setColor(productDetail.getColor().toString());
            dto.setSize(productDetail.getSize().toString());
            dto.setDisplayType(productDetail.getDisplayType().toString());
            dto.setPrice(productDetail.getPrice());
            dto.setOrderId(order.getOrderId());
            dto.setQuantity(orderDetail.getQuantity());
            dto.setProductPrice(productDetail.getPrice());

            // 생성한 DTO를 목록에 추가
            orderDetailListDto.add(dto);
        }

        return orderDetailListDto;
    }

}
