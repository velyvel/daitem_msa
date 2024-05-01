package org.daitem_msa.msa_user.repository;

import org.daitem_msa.msa_user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User>,
        UserRepositoryCustom {

    User findByEmail(String userEmail);

    @Query("SELECT u FROM User u WHERE u.loginId = :loginId")
    Optional<User> findByLoginId(@Param("loginId") String loginId);

}