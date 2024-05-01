package org.daitem_msa.msa_order.common.security;

import org.daitem_msa.msa_user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user", url = "http://localhost:8080")
public interface UserFeignClient {

    /**
     * @PathVariable : : id = loginId
     * */
    @GetMapping("/api/v1/user-order/login-id/{id}")
    Optional<User> findByLoginId(@PathVariable("id") String loginId);

    @GetMapping("/api/v1/user-order/id/{id}")
    Optional<User> findById(@PathVariable("id") Long id);
}
