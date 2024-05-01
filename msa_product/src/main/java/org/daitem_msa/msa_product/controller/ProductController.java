package org.daitem_msa.msa_product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_product.dto.*;
import org.daitem_msa.msa_product.service.ProductService;
import org.daitem_msa.msa_user.common.CommonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품 API")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1/product/add")
    @ResponseBody
    public CommonResponse<?> productAdd(@RequestBody ProductSaveDto productSaveDto) {
        try {
            productService.productAdd(productSaveDto);
            return CommonResponse.ok("상품 등록에 성공했습니다.");
        } catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 등록 상품 리스트 with 페이지네이션
     * 리스트에 필요한 정보는
     * (product)상품명, 상품 카테고리
     * (detail) 상품색상(옵션표시), 가격 상품 사진(이제 추가 해야함)
     *
     * @return productListDto
     * @Param page size, page
     */
    @GetMapping("api/v1/product")
    @Operation(parameters = {
            @Parameter(name = "category", description = "카테고리"),
            @Parameter(name = "field", description = "검색어"),
            @Parameter(name = "page", description = "페이지 번호"),
            @Parameter(name = "size", description = "한 페이지의 보여지는 목록 수")
    })
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<?> productList(@Parameter(hidden = true) SearchDto condition) {

        // 회원 있으면 디비에 장바구니, 회원 없으면 세션에 넣기: 이거의 시점 질문하기
        //condition.setUserId(user != null ? user.getUserId() : null);

        int setPage = 1, setSize = 10;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        Pageable pageable = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());
        //Page<ProductListDto> productList = productService.productList(condition, pageable);
        Page<ProductListDto> productList = productService.productList(condition, pageable);

        return CommonResponse.ok(productList);

    }

    /**
     * 상품 조회(상품 id 로 상세까지 조회)
     * @return productListDto
     * @PathVariable productId
     */

    @GetMapping("api/v1/product/product-and-detail/{id}")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<ProductDto> productDetail(@PathVariable Long id) {
        ProductDto productDto = productService.getProductsWithDetails(id);
        return CommonResponse.ok(productDto);
    }

    /**
     * 상품 수정 :  단건(옵션 수정할 경우), 다건(상품 명 등 product 테이블 수정하는 경우)
     * @param id 상품 아이디
     * @RequestBody ProductUpdateDto
     */
    @PutMapping("/api/v1/product/{id}")
    @ResponseBody
    public CommonResponse<?> updateProduct(@RequestBody ProductUpdateDto productUpdateDto,
                                           @PathVariable Long id) {
        try{
            productService.updateProduct(productUpdateDto, id);
            return new CommonResponse<>(200, "상품을 수정했습니다.");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }

    /**
     * 상품 리스트 페이지에서 삭제 : 상품 자체 삭제하는 경우
     * @param id 상품 아이디
     * */
    @DeleteMapping("/api/v1/product/{id}")
    @ResponseBody
    public CommonResponse<?> DeleteProduct(@PathVariable Long id) {
        try{
            productService.deleteProduct(id);
            return new CommonResponse<>(200, "상품을 삭제했습니다!.");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }
}
