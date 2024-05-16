package org.daitem_msa.msa_order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItems is a Querydsl query type for Items
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItems extends EntityPathBase<Items> {

    private static final long serialVersionUID = 2058202001L;

    public static final QItems items = new QItems("items");

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final NumberPath<Long> productDetailId = createNumber("productDetailId", Long.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QItems(String variable) {
        super(Items.class, forVariable(variable));
    }

    public QItems(Path<? extends Items> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItems(PathMetadata metadata) {
        super(Items.class, metadata);
    }

}

