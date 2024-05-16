package org.daitem_msa.msa_order.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.CommonResponse;
import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.daitem_msa.msa_order.dto.NewOrderSaveOneDto;
import org.daitem_msa.msa_order.entity.Item;
import org.daitem_msa.msa_order.service.OrderAndPayWithCacheService;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문 결제 컨트롤러 with cache", description = "cache 기반 API")
public class OrderAndPayWithRedisController {

    private final OrderAndPayWithCacheService cacheService;

    /**
     * redis items 추가
     * @RequestBody NewOrderSaveDto
     * */
    @PostMapping("/api/v2/order/product-add")
    public CommonResponse<?> redisProductAdd(@RequestBody Item items){
        try{
            cacheService.redisProductAdd(items);
            return CommonResponse.ok("레디스에 상품 저장 성공");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 레디스에 저장된 상품보기
     * */
    @GetMapping("/api/v2/order/product-view")
    public CommonResponse<Item> redisProductView(String itemId){
        try{
            Item items = cacheService.redisProductView(itemId);
            return CommonResponse.ok(items);
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }

    }

    /**
     * 구매 요청 api
     * @RequestBody NewOrderSaveDto
     * */
    @PostMapping("/api/v2/order/request")
    //@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> orderRequest(@RequestBody NewOrderSaveDto dto
                                          //,@AuthenticationPrincipal User user
    ){
//        if(user == null) {
//            throw new RuntimeException("사용자 확인이 되지 않습니다.");
//        }
        try{
            //user.setUserId(dto.getUserId());
            System.out.println("=======" + dto.getUserId() + "번 구매 요청");
            cacheService.orderRequest(dto);
            return CommonResponse.ok("주문 구매 요청");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }


    /**
     * 구매 요청 리스트 전체보기(관리자)
     * */
    @GetMapping("/api/v2/order/list")
    public CommonResponse<List<Map<Long, Integer>>> orderRequestTestResultWithRedisson(String itemId){
        List<Map<Long, Integer>> itemList = cacheService.resultWithRedisson(itemId);
        return CommonResponse.ok(itemList);
    }

    /**
     * 구매 요청
     * */
    @PostMapping("/api/v2/order/pay")
    public CommonResponse<?> orderPay(@RequestBody NewOrderSaveOneDto dto){
        try{
            cacheService.orderPay(dto);
            return CommonResponse.ok("결제 요청 시작");
        }
        catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 재고 확인 : redis 에서 조회하기, redis 에 정보 없으면 DB 에서 조회하도록 resilience4j 적용
     * */
    @GetMapping("/api/v2/order/show-stocks/{id}")
    public CommonResponse<?> orderShowStocks(@PathVariable String id){
        int stock = cacheService.showStock(id);
        return CommonResponse.ok(stock);
    }

}
