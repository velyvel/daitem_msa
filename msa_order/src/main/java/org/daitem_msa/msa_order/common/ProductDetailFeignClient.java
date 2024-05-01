package org.daitem_msa.msa_order.common;

import org.daitem_msa.msa_product.dto.ProductDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "detail", url = "http://localhost:8081")
public interface ProductDetailFeignClient {

    /**
     * 주문 - 상품 connect, 주문 시 상품 재고 빼기
     * @RequestBody Map<Long, Integer> map, 상품 상세 id 와 수량
     * */
    @PutMapping("/api/v1/order-to-product/stock_back_order")
    void updateStocksOrder(@RequestBody Map<Long, Integer> map);

    /**
     * 주문 - 상품 connect, 주문 시 상품 재고 돌려놓기
     * @RequestBody Map<Long, Integer> map, 상품 상세 id 와 수량
     * */
    @PutMapping("/api/v1/order-to-product/stock_back_return")
    void updateStocksReturn(@RequestBody Map<Long, Integer> map);

    /**
     * 주문 - 상품 connect, 상품 조회
     * @PathVariable id : 상품 상세 아이디
     * */
    @GetMapping("/api/v1/order-to-product-detail/{id}")
    ProductDetailDto findById(@PathVariable Long id);

}
