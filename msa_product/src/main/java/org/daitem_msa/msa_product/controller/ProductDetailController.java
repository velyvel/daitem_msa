package org.daitem_msa.msa_product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_product.dto.ProductDetailDtoOriginal;
import org.daitem_msa.msa_product.service.ProductDetailService;
import org.daitem_msa.msa_user.common.CommonResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품 상세", description = "상품 상세!!만 조정할 컨트롤러")
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    /**
     * 상품 상세 조회
     * @param id 상품 아이디
     * @return 상품 상세 정보 리스트
     */
    @GetMapping("/api/v1/product-detail/{id}")
    public CommonResponse<List<ProductDetailDtoOriginal>> getProductDetail(@PathVariable Long id) {
        List<ProductDetailDtoOriginal> productDetails = productDetailService.getProductDetail(id);
        return CommonResponse.ok(productDetails);
    }

    /**
     * 상품 상세 페이지에서 삭제 : 옵션들 삭제하는 경우
     * @param id 상품 상세 아이디
     * */
    @DeleteMapping("/api/v1/product-detail/{id}")
    @ResponseBody
    public CommonResponse<?> DeleteProductDetail(@PathVariable Long id) {
        try{
            productDetailService.deleteProductDetail(id);
            return new CommonResponse<>(200, "상품 옵션 삭제 완료!!.");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

}
