package org.daitem_msa.msa_product.repository;


import org.daitem_msa.msa_product.dto.WishListListDto;

import java.util.List;

public interface WishListRepositoryCustom {
    List<WishListListDto> findByUserId(Long userId);
}
