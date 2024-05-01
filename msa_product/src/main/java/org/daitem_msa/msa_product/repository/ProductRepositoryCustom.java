package org.daitem_msa.msa_product.repository;


import org.daitem_msa.msa_product.dto.ProductListDto;
import org.daitem_msa.msa_product.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<ProductListDto> productList(SearchDto condition, Pageable pageable);
}
