package org.daitem_msa.msa_order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.daitem_msa.msa_order.enumset.OrderStatus;
import org.daitem_msa.msa_order.enumset.PaymentMethod;
import org.daitem_msa.msa_order.enumset.YN;
import org.daitem_msa.msa_user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    private Long userId;

    private LocalDateTime orderDate;

    // 주문한 상품 총 개수 -> 상품 하나에 대한 개수랑은 다름
    private int totalAmount;

    @Convert(converter = OrderStatus.OrdersConverter.class)
    private OrderStatus orderStatus;

    @Convert(converter = PaymentMethod.PaymentMethodConverter.class)
    private PaymentMethod paymentMethod;

    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingAddress3;

    @Convert(converter = YN.YNConverter.class)
    private YN isDelivered;

    private LocalDateTime deliveryDate;

    // lazy type 으로 제공되는데, eager 도 성능상 이점이 있는지 고려하기
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

}
