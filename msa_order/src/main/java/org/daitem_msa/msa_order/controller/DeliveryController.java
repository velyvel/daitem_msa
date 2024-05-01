package org.daitem_msa.msa_order.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.CommonResponse;
import org.daitem_msa.msa_order.service.DeliveryService;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "배송", description = "배송 API")
public class DeliveryController {

    private final DeliveryService deliveryService;

    /***
     * 반품 api
     * 배송 완료 후 + 1 일 까지 취소할 수 있다(상태변경)
     * 상태 변경 후 productDetail 재고 + amount
     * @Param id 주문Id
     * */
    // isDelivered Y, deliveryDate.plusDays(1L).before ->
    @DeleteMapping("/api/v1/delivery/{id}")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse CanceledDelivery(@PathVariable Long id,
                                           @AuthenticationPrincipal User user) {
        try{
            deliveryService.CanceledDelivery(user, id);
            return CommonResponse.ok("배송 취소 성공 !");
        }catch (Exception e){
            return new CommonResponse<>(500, e.getMessage());
        }
    }

}
