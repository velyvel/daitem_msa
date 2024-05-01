package org.daitem_msa.msa_product.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_product.dto.ProductListDto;
import org.daitem_msa.msa_product.dto.SearchDto;
import org.daitem_msa.msa_product.enumset.DisplayType;
import org.daitem_msa.msa_user.enumset.YN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.daitem_msa.msa_product.entity.QProduct.product;
import static org.daitem_msa.msa_product.entity.QProductDetail.productDetail;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductListDto> productList(SearchDto condition, Pageable pageable) {
        List<ProductListDto> content = queryFactory
                .select(Projections.fields(ProductListDto.class,
                        product.productId,
                        product.productName,
                        product.productCategory,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", productDetail.color))
                                //.from(productDetail)
                                //.where(productDetail.product.productId.eq(product.productId))
                                , "colors")
                ))
                .from(product)
                .leftJoin(productDetail).on(productDetail.product.productId.eq(product.productId))
                .where(productDetail.displayType.eq(DisplayType.DISPLAYED).and(product.isRealDelete.eq(YN.N)))
                .groupBy(product.productId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(product)
                .leftJoin(productDetail).on(productDetail.product.productId.eq(product.productId))
                .where(productDetail.displayType.eq(DisplayType.DISPLAYED))
                .groupBy(product.productId);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }
}
