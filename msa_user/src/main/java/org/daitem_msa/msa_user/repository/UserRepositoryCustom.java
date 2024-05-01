package org.daitem_msa.msa_user.repository;

import org.daitem_msa.msa_user.dto.UserDto;

public interface UserRepositoryCustom {
    UserDto searchUserBy(Long userId);
}
