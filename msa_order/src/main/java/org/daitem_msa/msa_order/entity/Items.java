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

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Data
@Table(name = "Items")
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private Long userId;

    // 상품 상세 아이디
    private Long productDetailId;

    // 재고
    private int stock;

}
