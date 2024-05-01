package org.daitem_msa.msa_order.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.CommonResponse;
import org.daitem_msa.msa_order.dto.OrderListDto;
import org.daitem_msa.msa_order.dto.OrderSaveDto;
import org.daitem_msa.msa_order.service.OrderService;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문", description = "주문 API")
public class OrderController {

    private final OrderService orderService;
    /**
     * 주문 api,
     * totalAmount : 주문하는 상품의 총 개수, 0개 이하 입력 시 RuntimeException : 상품 한 개 이상 주문하세요
     * */
    @PostMapping("/api/v1/order")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> productAdd(@RequestBody OrderSaveDto dto,
                                        @AuthenticationPrincipal User user) {
        try {
            if(user == null) {
                throw new RuntimeException("회원가입을 먼저 해 주세요");
            }
            dto.setUserId(user.getUserId());
            orderService.orderAdd(dto);
            return CommonResponse.ok("주문에 성공했습니다!");
        } catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 주문 리스트 api
     * */
    @GetMapping("/api/v1/order")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> orderList(@AuthenticationPrincipal User user) {

        if(user == null) {
            throw new RuntimeException("잘못된 접근입니다. 다시 로그인 해 주세요");
        }
        Long userId = user.getUserId();
        List<OrderListDto> orderList = orderService.orderList(userId);
        return CommonResponse.ok(orderList);
    }

    /***
     * 주문 취소 api
     * @Param id 주문 id
     * */
    @DeleteMapping("/api/v1/order/{id}")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> orderCanceled(@AuthenticationPrincipal User user,
                                           @PathVariable Long id) {

        if(user == null) {
            throw new RuntimeException("잘못된 접근입니다. 다시 로그인 해 주세요");
        }

        try {
            orderService.orderCanceled(id);
            return CommonResponse.ok("주문 취소 성공!!");
        } catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }
}
