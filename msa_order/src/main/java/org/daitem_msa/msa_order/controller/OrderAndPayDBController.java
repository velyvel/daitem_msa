package org.daitem_msa.msa_order.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.CommonResponse;
import org.daitem_msa.msa_order.dto.NewOrderItemDto;
import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.daitem_msa.msa_order.dto.NewOrderSaveOneDto;
import org.daitem_msa.msa_order.dto.OrderSaveDto;
import org.daitem_msa.msa_order.service.OrderAndPayWithDBService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문 결제 컨트롤러 with DB", description = "데이터베이스 기반 동시성 처리 API")
public class OrderAndPayDBController {

    private final OrderAndPayWithDBService orderDBService;

    /**
     * real start here
     * end points (구매 요청, 주문 생성 준비, 신규 주문 이벤트, 신규 주문 이벤트수신,
     * 결제 및 출금 API, 결제 성공 이벤트 발행 API, 결제 성공 이벤트 수신 API: 주문 생성 API:
     */

    /**
     * Item 테이블에 추가
     * @RequestBody NewOrderSaveDto
     * */
    @PostMapping("/api/v1/order/product-add")
    public CommonResponse<?> productAdd(@RequestBody NewOrderItemDto dto){
        try{
            orderDBService.productAdd(dto);
            return CommonResponse.ok("상품테이블 저장 완료");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }


    /**
     * 구매 요청 with DB
     * */
    @PostMapping("/api/v1/order/request")
    //@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> orderRequest(@RequestBody NewOrderSaveDto dto){
        try{
            System.out.println("=======" + dto.getUserId() + "번 분산락 구매 요청");
            orderDBService.orderRequest(13L, 1, dto);
            return CommonResponse.ok("주문 구매 요청");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 결제 요청 api
     * */
    @PostMapping("/api/v1/order/pay")
    public CommonResponse<?> orderPay(@RequestBody NewOrderSaveOneDto dto){
        try{
            orderDBService.orderPay(dto);
            return CommonResponse.ok("결제 요청 시작");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 재고 확인
     * @PathVariable id: 상품아이디
     * */
    @GetMapping("/api/v1/order/show-stocks/{id}")
    public CommonResponse<?> showStocks(@PathVariable Long id){
        int stock = orderDBService.showStocks(id);
        return CommonResponse.ok(stock);
    }
}
