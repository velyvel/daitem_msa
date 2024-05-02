package org.daitem_msa.msa_product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_product.dto.ProductDetailDto;
import org.daitem_msa.msa_product.entity.ProductDetail;
import org.daitem_msa.msa_product.repository.ProductDetailRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewProductService {

    private final ProductDetailRepository productDetailRepository;

    @Transactional
    public ProductDetail showProductDetail(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품 상세정보 조회할 수 없다"));
        return productDetail;
    }
}
