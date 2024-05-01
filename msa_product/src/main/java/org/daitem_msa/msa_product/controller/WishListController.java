package org.daitem_msa.msa_product.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_product.dto.WishListListDto;
import org.daitem_msa.msa_product.dto.WishListSaveDto;
import org.daitem_msa.msa_product.service.WishListService;
import org.daitem_msa.msa_user.common.CommonResponse;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "위시리스트, 장바구니", description = "구매 전 임시저장, 상품, 수량 변경할 수 있는 페이지, 일단 회원만 주문 가능 -> 추후 비회원 구현 예정")
public class WishListController {
    private final WishListService wishListService;
    /**
     * 장바구니 등록 : 단건등록만, 로직 상 상품 상세, 혹은 상품 리스트 페이지 하트누르기로 구현
     * */
    @PostMapping("/api/v1/cart")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> addWishList(@RequestBody WishListSaveDto dto,
                                         @AuthenticationPrincipal User user) {
        try {
            wishListService.addWishList(dto, user);
            return CommonResponse.ok("장바구니 추가 성공!");
        } catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 장바구니 조회
     * @Param id, 회원 아이디
     * */
    @GetMapping("/api/v1/cart")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> listWishList(@AuthenticationPrincipal User user) {
        List<WishListListDto> wishList = wishListService.listWishList(user);
        return CommonResponse.ok(wishList);
    }

    /**
     * 질문하기!
     * 장바구니 수정(수량 변경 plus)
     * @Param id : 상품 상세 아이디
     * */
    @PutMapping("/api/v1/cart/plus/{id}")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> updateWishListPlus(@PathVariable Long id) {
        try{
            wishListService.updateWishListPlus(id);
            return new CommonResponse<>(200, "수량 변경 완료.");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 질문하기!
     * 장바구니 수정(수량 변경 minus)
     * @Param id : 상품 상세 아이디
     * */
    @PutMapping("/api/v1/cart/minus/{id}")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> updateWishListMinus(@PathVariable Long id) {
        try{
            wishListService.updateWishListMinus(id);
            return new CommonResponse<>(200, "수량 변경 완료.");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 장바구니 삭제 : 상품상세 id 별 삭제 -> 옵션별 삭제
     * @Param id : 상품상세 id
     * */
    @DeleteMapping("/api/v1/cart/{id}")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> deleteWishList(@PathVariable Long id) {
        try{
            wishListService.deleteWishList(id);
            return new CommonResponse<>(200, "상품 id " + id + "번 장바구니 삭제");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 장바구니 삭제 : 장바구니 리스트 일괄 삭제
     * */
    @DeleteMapping("/api/v1/cart/all")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> deleteWishListAll(@AuthenticationPrincipal User user) {
        try{
            wishListService.deleteWishListAll(user);
            return new CommonResponse<>(200, "사용자 " + user.getUserId() + "번 장바구니 삭제");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }
}
