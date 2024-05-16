package org.daitem_msa.msa_order.entity;

import jakarta.persistence.*;
import lombok.*;

//redis Template 방식)
@Getter
@NoArgsConstructor
@Data
public class Item {
    @Id
    private String itemId;
    // 상품 상세 아이디
    private Long productDetailId;
    // 재고
    private int stock;
}
