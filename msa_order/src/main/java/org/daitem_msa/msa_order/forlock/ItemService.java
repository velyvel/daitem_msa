package org.daitem_msa.msa_order.forlock;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.entity.Item;
import org.daitem_msa.msa_order.entity.ItemHistory;
import org.daitem_msa.msa_order.entity.Items;
import org.daitem_msa.msa_order.repository.ItemsHistoryRepository;
import org.daitem_msa.msa_order.repository.ItemsRepository;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {

    private final ItemsRepository itemsRepository;
    private final ItemsHistoryRepository historyRepository;

    @Transactional
    public void forOptimistic(LockDto dto) {
        // 상품을 조회한다
        Items items = itemsRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // 재고 확인
        if (items.getStock() < dto.getStock()) {
            throw new RuntimeException("Stock limit exceeded");
        }

        // 상품 히스토리 테이블에 구입한 사람들 저장한다
        ItemHistory history = ItemHistory.builder()
                .userId(dto.getUserId())
                .stock(items.getStock())
                .itemId(dto.getItemId())
                .build();
        historyRepository.save(history);
        items.prePersist();

        itemsRepository.save(items);
    }

    public void forPessimistic(LockDto dto) {
        Items items = itemsRepository.findById2(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // 재고 확인
        if (items.getStock() < dto.getStock()) {
            throw new RuntimeException("Stock limit exceeded");
        }

        ItemHistory history = ItemHistory.builder()
                .userId(dto.getUserId())
                .stock(items.getStock())
                .itemId(dto.getItemId())
                .build();
        historyRepository.save(history);
        items.prePersist();
        itemsRepository.save(items);

    }
}
