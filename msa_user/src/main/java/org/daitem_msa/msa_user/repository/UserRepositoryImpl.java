package org.daitem_msa.msa_user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_user.dto.UserDto;
import org.springframework.stereotype.Repository;

import static org.daitem_msa.msa_user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public UserDto searchUserBy(Long userId) {
        return queryFactory
                .select(Projections.fields(UserDto.class,
                        user.userId,
                        user.loginId,
                        user.password,
                        user.userRole,
                        user.userName,
                        user.email,
                        user.address1,
                        user.address2,
                        user.address3,
                        user.phone
                ))
                .from(user)
                .where(user.userId.eq(userId))
                .fetchFirst();
    }
}
