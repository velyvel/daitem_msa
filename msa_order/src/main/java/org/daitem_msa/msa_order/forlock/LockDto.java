package org.daitem_msa.msa_order.forlock;

import lombok.Data;

@Data
public class LockDto {
    private Long itemId;
    // 이건 save 할 때 넣자
    //private Long itemHistoryId;
    private Long userId;
    private int stock;
    private Long productDetailId;
}
