package org.daitem_msa.msa_order.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.CommonResponse;
import org.daitem_msa.msa_order.dto.OrderDetailListDto;
import org.daitem_msa.msa_order.service.OrderDetailService;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문 상세", description = "주문 상세 API")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    /**
     * 주문 상세 api
     * @Param id 주문 id
     * */
    @GetMapping("/api/v1/order_detail/{id}")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> orderDetailList(@AuthenticationPrincipal User user, @PathVariable Long id) {
        if(user == null) {
            throw new RuntimeException("회원 조회 실패");
        }
        // id : orderId
        List<OrderDetailListDto> orderDetails = orderDetailService.getOrderDetails(id);
        return CommonResponse.ok(orderDetails);
    }

}
