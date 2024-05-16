package org.daitem_msa.msa_order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 2063682335L;

    public static final QOrder order = new QOrder("order1");

    public final DateTimePath<java.time.LocalDateTime> deliveryDate = createDateTime("deliveryDate", java.time.LocalDateTime.class);

    public final EnumPath<org.daitem_msa.msa_order.enumset.YN> isDelivered = createEnum("isDelivered", org.daitem_msa.msa_order.enumset.YN.class);

    public final DateTimePath<java.time.LocalDateTime> orderDate = createDateTime("orderDate", java.time.LocalDateTime.class);

    public final ListPath<OrderDetail, QOrderDetail> orderDetails = this.<OrderDetail, QOrderDetail>createList("orderDetails", OrderDetail.class, QOrderDetail.class, PathInits.DIRECT2);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final EnumPath<org.daitem_msa.msa_order.enumset.OrderStatus> orderStatus = createEnum("orderStatus", org.daitem_msa.msa_order.enumset.OrderStatus.class);

    public final EnumPath<org.daitem_msa.msa_order.enumset.PaymentMethod> paymentMethod = createEnum("paymentMethod", org.daitem_msa.msa_order.enumset.PaymentMethod.class);

    public final StringPath shippingAddress1 = createString("shippingAddress1");

    public final StringPath shippingAddress2 = createString("shippingAddress2");

    public final StringPath shippingAddress3 = createString("shippingAddress3");

    public final NumberPath<Integer> totalAmount = createNumber("totalAmount", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

