package org.daitem_msa.msa_product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_product.dto.*;
import org.daitem_msa.msa_product.entity.Product;
import org.daitem_msa.msa_product.entity.ProductDetail;
import org.daitem_msa.msa_product.enumset.Colors;
import org.daitem_msa.msa_product.enumset.DisplayType;
import org.daitem_msa.msa_product.enumset.Size;
import org.daitem_msa.msa_product.repository.ProductDetailRepository;
import org.daitem_msa.msa_product.repository.ProductRepository;
import org.daitem_msa.msa_user.enumset.YN;
import org.daitem_msa.msa_user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    @Transactional
    public void productAdd(ProductSaveDto productSaveDto) {
        Product product = Product.builder()
                .productName(productSaveDto.getProductName())
                .productCategory(productSaveDto.getCategories())
                .description(productSaveDto.getDescription())
                .isRealDelete(YN.N)
                .build();

        // Product 저장
        productRepository.save(product);

        // ProductDetail 생성 및 저장
        for (ProductSaveDto.ProductDetailDto detailDto : productSaveDto.getProductDetails()) {

            // 판매기간 설정이 안되었는데, 판매 기간이 지정되어 있으면 runtime Exception
            if (detailDto.getIsSaleTerms() == YN.N &&
                    (detailDto.getSalesToDate() != null || detailDto.getSalesFromDate() != null)) {
                throw new RuntimeException("판매 기간 설정을 한 후 기간을 정해주세요!");
            }

            //재고 0으로 등록하려는 경우
            if (detailDto.getStock() < 0) {
                throw new RuntimeException("재고는 하나 이상 등록해 주세요.");
            }

            if (detailDto.getDisplayType() == DisplayType.DELETED
                    || detailDto.getDisplayType() == DisplayType.SOLDOUT) {
                throw new RuntimeException("숨김과 보임만 설정할 수 있습니다");
            }

            ProductDetail productDetail = ProductDetail.builder()
                    .product(product)
                    .price(detailDto.getPrice())
                    .color(Colors.ofColor(String.valueOf(detailDto.getColor())))
                    .size(Size.ofSize(String.valueOf(detailDto.getSize())))
                    .stock(detailDto.getStock())
                    .isSaleTerms(detailDto.getIsSaleTerms())
                    .saleFromDate(detailDto.getSalesFromDate())
                    .saleToDate(detailDto.getSalesToDate())
                    .price(detailDto.getPrice())
                    .displayType(detailDto.getDisplayType())
                    .isDeleted(detailDto.getIsDeleted())
                    .build();
            productDetailRepository.save(productDetail);
        }
    }

    public Page<ProductListDto> productList(SearchDto condition, Pageable pageable) {
        return productRepository.productList(condition, pageable);
    }

    @Transactional
    public ProductDto getProductsWithDetails(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품이 조회되지 않습니다."));

        Long productId = product.getProductId();

        List<ProductDetailDtoOriginal> productDetails = productDetailRepository.findByProductId(productId);
        List<ProductDetailDto> productDetailDtos = productDetails.stream()
                .map(this::mapToProductDetailDto)
                .collect(Collectors.toList());

        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setProductCategory(product.getProductCategory());
        productDto.setDescription(product.getDescription());
        productDto.setProductDetails(productDetailDtos);

        return productDto;
    }

    //상세 > 리스트 조회하는 매서드
    private ProductDetailDto mapToProductDetailDto(ProductDetailDtoOriginal productDetail) {
        ProductDetailDto productDetailDto = new ProductDetailDto();

        productDetailDto.setProductId(productDetail.getProductId());
        productDetailDto.setProductDetailId(productDetail.getProductDetailId());
        productDetailDto.setColor(productDetail.getColor());
        productDetailDto.setSize(productDetail.getSize());
        productDetailDto.setStock(productDetail.getStock());
        productDetailDto.setPrice(productDetail.getPrice());
        productDetailDto.setDisplayType(productDetail.getDisplayType());
        productDetailDto.setIsDeleted(productDetail.getIsDeleted());

        return productDetailDto;
    }

    @Transactional
    public void updateProduct(ProductUpdateDto productUpdateDto, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품이 조회되지 않습니다."));

        // 상품 정보 업데이트
        product.setProductName(productUpdateDto.getProductName());
        product.setProductCategory(productUpdateDto.getCategories());
        product.setDescription(productUpdateDto.getDescription());
        productRepository.save(product);

        // 상품 상세 정보 조회
        List<ProductDetailDtoOriginal> productDetails = productDetailRepository.findByProductId(id);
        Map<Long, ProductDetailDtoOriginal> originalDetailMap = productDetails.stream()
                .collect(Collectors.toMap(ProductDetailDtoOriginal::getProductDetailId, Function.identity()));

        // 추가된 상세 정보 생성
        List<ProductDetailUpdateDto> updatedDetails = productUpdateDto.getProductDetails();
        for (ProductDetailUpdateDto updatedDetail : updatedDetails) {
            Long productDetailId = updatedDetail.getProductDetailId();
            if (productDetailId == null) {
                // 새로 추가된 상세 정보인 경우 생성
                ProductDetailDtoOriginal originalDetail = originalDetailMap.get(productDetailId);
                if (originalDetail == null) {
                    ProductDetail newProductDetail = createNewDetail(product, updatedDetail);
                    productDetailRepository.save(newProductDetail);
                }
            } else {
                // 기존 상세 정보 업데이트
                ProductDetail existingProductDetail = productDetailRepository.findById(productDetailId)
                        .orElseThrow(() -> new RuntimeException("상품 상세 정보가 조회되지 않습니다."));

                existingProductDetail.setColor(updatedDetail.getColor());
                existingProductDetail.setSize(updatedDetail.getSize());
                existingProductDetail.setStock(updatedDetail.getStock());
                existingProductDetail.setPrice(updatedDetail.getPrice());
                existingProductDetail.setDisplayType(updatedDetail.getDisplayType());
                existingProductDetail.setIsDeleted(updatedDetail.getIsDeleted());

                productDetailRepository.save(existingProductDetail);
            }
        }
    }

    private ProductDetail createNewDetail(Product product, ProductDetailUpdateDto updatedDetail) {
        return ProductDetail.builder()
                .product(product)
                .color(updatedDetail.getColor())
                .size(updatedDetail.getSize())
                .stock(updatedDetail.getStock())
                .price(updatedDetail.getPrice())
                .displayType(updatedDetail.getDisplayType())
                .isDeleted(updatedDetail.getIsDeleted())
                .build();
    }


    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품이 조회되지 않습니다."));
        product.setIsRealDelete(YN.Y);
        productRepository.save(product);

        // 상품 상세 정보 조회
        List<ProductDetail> productDetails = productDetailRepository.findByProduct(product);

        // 변경된 상품 상세 정보를 저장할 리스트
        List<ProductDetail> updatedProductDetails = new ArrayList<>();

        for (ProductDetail productDetail : productDetails) {
            productDetail.setIsDeleted(YN.Y);
            productDetail.setDisplayType(DisplayType.DELETED);
            updatedProductDetails.add(productDetail); // 변경된 객체를 리스트에 추가
        }

        // 변경된 상품 상세 정보를 일괄적으로 저장
        productDetailRepository.saveAll(updatedProductDetails);
    }

    @Transactional
    public void deleteProductDetail(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상세 상품이 없어요"));
        productDetail.setIsDeleted(YN.Y);
        productDetail.setDisplayType(DisplayType.DELETED);
    }


    public Product connectToOrder(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품"));
    }

//    @Transactional
//    public void putStocksBack(List<OrderDetail> orderDetails) {
//        for (OrderDetail orderDetail : orderDetails) {
//            ProductDetail productDetail = productDetailRepository.findById(orderDetail.getProductDetailId())
//                    .orElseThrow(() -> new RuntimeException("상품 정보를 찾을 수 없습니다."));
//            productDetail.setStock(productDetail.getStock() + orderDetail.getQuantity());
//            productDetailRepository.save(productDetail);
//        }
//    }
}
