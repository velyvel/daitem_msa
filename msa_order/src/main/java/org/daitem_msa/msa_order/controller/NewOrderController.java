package org.daitem_msa.msa_order.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.CommonResponse;
import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.daitem_msa.msa_order.dto.OrderSaveDto;
import org.daitem_msa.msa_order.service.NewOrderService;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문 test", description = "주문 test API")
public class NewOrderController {

    private final NewOrderService newOrderService;

    /**
     * 주문 api,
     * totalAmount : 주문하는 상품의 총 개수, 0개 이하 입력 시 RuntimeException : 상품 한 개 이상 주문하세요
     * */
    @PostMapping("/api/v1/new_order")
    //@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> productAdd(@RequestBody NewOrderSaveDto dto
                                        //@AuthenticationPrincipal User user
                                        ) {
        try {
            if(dto.getUserId() == null) {
                throw new RuntimeException("회원가입을 먼저 해 주세요");
            }
            System.out.println("요청이 갔나 ? ");
            //dto.setUserId(user.getUserId());
            newOrderService.newOrderAdd(dto);
            return CommonResponse.ok("주문에 성공했습니다!");
        } catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * real start here
     * end points (구매 요청, 주문 생성 준비, 신규 주문 이벤트, 신규 주문 이벤트수신,
     * 결제 및 출금 API, 결제 성공 이벤트 발행 API, 결제 성공 이벤트 수신 API: 주문 생성 API:
     */

    /**
     * 구매 요청 api
     * @Param id : productDetailId
     * */
    @PostMapping("/api/v1/order/request/{id}")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> orderRequest(@AuthenticationPrincipal User user, @PathVariable Long id){
        try{
            id = 12L;
            newOrderService.orderRequest(user, id);
            return CommonResponse.ok("주문 구매 요청");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

}
