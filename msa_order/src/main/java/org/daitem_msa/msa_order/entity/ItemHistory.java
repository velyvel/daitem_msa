package org.daitem_msa.msa_order.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Data
@Table(name = "item_history")
public class ItemHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemHistoryId;
    // 상품 상세 아이디
    private Long itemId;
    // 사용자 아이디
    private Long userId;
    // 재고
    private int stock;
}
