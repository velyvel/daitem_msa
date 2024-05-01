package org.daitem_msa.msa_product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    /**
     * 회원 시퀀스
     */
    @Schema(hidden = true)
    private Long userId;

    @Schema(description = "카테고리")
    private String category;

    @Schema(description = "검색어")
    private String field;

    @Schema(description = "페이지 번호")
    private String page;

    @Schema(description = "목록의 수")
    private String size;
}