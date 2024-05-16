package org.daitem_msa.msa_product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1031138751L;

    public static final QProduct product = new QProduct("product");

    public final StringPath description = createString("description");

    public final EnumPath<org.daitem_msa.msa_user.enumset.YN> isRealDelete = createEnum("isRealDelete", org.daitem_msa.msa_user.enumset.YN.class);

    public final EnumPath<org.daitem_msa.msa_product.enumset.Categories> productCategory = createEnum("productCategory", org.daitem_msa.msa_product.enumset.Categories.class);

    public final ListPath<ProductDetail, QProductDetail> productDetails = this.<ProductDetail, QProductDetail>createList("productDetails", ProductDetail.class, QProductDetail.class, PathInits.DIRECT2);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productName = createString("productName");

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

