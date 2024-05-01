package org.daitem_msa.msa_product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_product.dto.ProductDetailDto;
import org.daitem_msa.msa_product.service.ProductDetailService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품 상세 - 주문 feign controller", description = "상품 상세 -> 주문 간 연관 컨트롤러")
public class ProductDetailOrderController {

    private final ProductDetailService productDetailService;

    /**
     * 주문 - 상품 connect, 주문 시 상품 재고 빼기
     * @RequestBody Map<Long, Integer> map, 상품 상세 id 와 수량
     * */
    @PutMapping("/api/v1/order-to-product/stock_back_order")
    public void stockBackOrder(@RequestBody Map<Long, Integer> map) {
        productDetailService.stockBackOrder(map);
    }

    /**
     * 주문 - 상품 connect, 주문 시 상품 재고 돌려놓기
     * @RequestBody Map<Long, Integer> map, 상품 상세 id 와 수량
     * */
    @PutMapping("/api/v1/order-to-product/stock_back_return")
    public void stockBackReturn(@RequestBody Map<Long, Integer> map) {
        productDetailService.stockBackReturn(map);
    }

    /**
     * 주문 - 상품 connect, 상품 조회
     * @PathVariable id : 상품 상세 아이디
     * */
    @GetMapping("/api/v1/order-to-product-detail/{id}")
    public ProductDetailDto findById(@PathVariable Long id) {
        ProductDetailDto productDetail = productDetailService.findById(id);
        return productDetail;
    }

}
