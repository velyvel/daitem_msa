package org.daitem_msa.msa_product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_product.dto.ProductDetailDto;
import org.daitem_msa.msa_product.dto.ProductDetailDtoOriginal;
import org.daitem_msa.msa_product.entity.Product;
import org.daitem_msa.msa_product.entity.ProductDetail;
import org.daitem_msa.msa_product.enumset.DisplayType;
import org.daitem_msa.msa_product.repository.ProductDetailRepository;
import org.daitem_msa.msa_product.repository.ProductRepository;
import org.daitem_msa.msa_user.enumset.YN;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;

    @Transactional
    public List<ProductDetailDtoOriginal> getProductDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품이 조회되지 않습니다."));

        return productDetailRepository.findByProductId(product.getProductId());
    }

    @Transactional
    public void deleteProductDetail(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상세 상품이 없어요"));
        productDetail.setIsDeleted(YN.Y);
        productDetail.setDisplayType(DisplayType.DELETED);
    }


    public void save(ProductDetail productDetail) {
        productDetailRepository.save(productDetail);
    }

    public ProductDetail connectToOrder(long id) {
        return productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문에서 넘어온 상품상세 fail"));
    }

    //주문 하면 재고에서 빼기
    public void stockBackOrder(Map<Long, Integer> map) {
        for (Map.Entry<Long, Integer> entry : map.entrySet() ) {
           ProductDetail productDetail =
                   productDetailRepository.findById(entry.getKey())
                           .orElseThrow(() -> new RuntimeException("주문 상세정보 없습니다"));
           productDetail.setStock(productDetail.getStock() - entry.getValue());
           productDetailRepository.save(productDetail);
        }
    }

    //주문 취소 or 반품하면 재고 돌려놓기
    public void stockBackReturn(Map<Long, Integer> map) {
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            ProductDetail productDetail =
                    productDetailRepository.findById(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("주문 상세정보 없습니다"));
            productDetail.setStock(productDetail.getStock() + entry.getValue());
            productDetailRepository.save(productDetail);
        }
    }

    public ProductDetailDto findById(Long id) {
        ProductDetailDto productDetailDto = new ProductDetailDto();
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품 상세정보가 조회되지 않습니다."));

//        Product product = productRepository.findByProductIdAndProductDetailId(productDetailDto.getProductId(),
//                        productDetailDto.getProductDetailId()).orElseThrow(() -> new RuntimeException("상품 정보가 조회되지 않습니다."));

        Product product = productRepository.findById(productDetail.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("상품 정보가 조회되지 않습니다."));
        productDetailDto.setProductId(product.getProductId());
        productDetailDto.setProductName(product.getProductName());
        productDetailDto.setDescription(product.getDescription());
        productDetailDto.setDisplayType(productDetail.getDisplayType());
        productDetailDto.setProductDetailId(productDetail.getProductDetailId());
        productDetailDto.setSize(productDetail.getSize());
        productDetailDto.setPrice(productDetail.getPrice());
        productDetailDto.setColor(productDetail.getColor());

        return productDetailDto;
    }
}
