package org.daitem_msa.msa_product.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_product.dto.ProductDetailDtoOriginal;
import org.daitem_msa.msa_product.enumset.DisplayType;
import org.daitem_msa.msa_user.enumset.YN;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.daitem_msa.msa_product.entity.QProduct.product;
import static org.daitem_msa.msa_product.entity.QProductDetail.productDetail;

@Repository
@RequiredArgsConstructor
public class ProductDetailRepositoryImpl implements ProductDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductDetailDtoOriginal> findByProductId(Long productId) {
        List<ProductDetailDtoOriginal> content = queryFactory
                .select(Projections.fields(ProductDetailDtoOriginal.class,
                        productDetail.product.productId,
                        productDetail.productDetailId,
                        productDetail.color,
                        productDetail.size,
                        productDetail.stock,
//                        productDetail.isSaleTerms,
//                        productDetail.saleFromDate,
//                        productDetail.saleToDate,
                        productDetail.price,
                        productDetail.displayType,
                        productDetail.isDeleted
                ))
                .from(productDetail)
                .where(productDetail.displayType.eq(DisplayType.DISPLAYED)
                        .or(productDetail.displayType.eq(DisplayType.SOLDOUT))
                        .and(productDetail.isDeleted.eq(YN.N))
                        .and(product.productId.eq(productId)))
                .leftJoin(product).on(product.productId.eq(productDetail.product.productId))
                .fetch();

        return content;
    }
}
