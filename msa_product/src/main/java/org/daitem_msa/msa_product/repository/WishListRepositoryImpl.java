package org.daitem_msa.msa_product.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_product.dto.WishListListDto;
import org.daitem_msa.msa_product.enumset.DisplayType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.daitem_msa.msa_product.entity.QProductDetail.productDetail;
import static org.daitem_msa.msa_product.entity.QWishList.wishList;


@Repository
@RequiredArgsConstructor
public class WishListRepositoryImpl implements WishListRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<WishListListDto> findByUserId(Long userId) {
        // 기록해두기 : 해결
        //QProductDetail d2 = new QProductDetail(productDetail);
        List<WishListListDto> content = queryFactory
                .select(Projections.fields(WishListListDto.class,
                        wishList.product.productId,
                        //wishList.user.userId,
                        wishList.userId,
                        wishList.product.productName,
                        wishList.product.description,
                        wishList.product.productCategory,
                        wishList.productDetail.productDetailId,
                        wishList.productDetail.color,
                        wishList.productDetail.size,
                        wishList.productDetail.price,
                        wishList.count,
                        wishList.productDetail.displayType

//                        ExpressionUtils.as(
//                                JPAExpressions
//                                        .select(wishList.count())
//                                        .from(wishList)
//                                        .where(wishList.productDetail.productDetailId
//                                                .eq(productDetail.productDetailId)
//                                                .and(wishList.user.userId.eq(userId))),
//                                "count"
//                        )
                ))
                .from(wishList)
                //.join(d2).on(d2.productDetailId.eq(wishList.productDetail.productDetailId))
                .where(productDetail.displayType.eq(DisplayType.DISPLAYED)
                        .or(productDetail.displayType.eq(DisplayType.SOLDOUT))
                        .and(wishList.userId.eq(userId)))
//                .groupBy(wishList.product.productId,
//                        wishList.user.userId,
//                        wishList.product.productName,
//                        wishList.product.description,
//                        wishList.product.productCategory,
//                        wishList.productDetail.productDetailId,
//                        wishList.productDetail.color,
//                        wishList.productDetail.size,
//                        wishList.productDetail.price,
//                        wishList.productDetail.displayType)
                .fetch();

        return content;
    }
}
