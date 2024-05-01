package org.daitem_msa.msa_product.repository;

import org.daitem_msa.msa_product.dto.ProductDetailDtoOriginal;

import java.util.List;

public interface ProductDetailRepositoryCustom {
    List<ProductDetailDtoOriginal> findByProductId(Long productId);
}
