package org.daitem_msa.msa_product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_product.dto.ProductDetailDto;
import org.daitem_msa.msa_product.entity.ProductDetail;
import org.daitem_msa.msa_product.service.NewProductService;
import org.daitem_msa.msa_user.common.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품 new", description = "대용량 트래픽 처리를 위한 상품 new")
public class NewProductController {

    private final NewProductService newProductService;

    /**기존 : 상품상세 -> 상품상세별 리스트(색상,사이즈) 별 리스트 조회
     * 바꿈 : 상품 상세 id 하나로 조회
     * @PathVariable id: productDetailId
     * */

    @GetMapping("api/v1/new-product-detail/{id}")
   // @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<ProductDetail> showProductDetail(@PathVariable Long id) {
        ProductDetail productDetail = newProductService.showProductDetail(id);
        return CommonResponse.ok(productDetail);
    }
}
