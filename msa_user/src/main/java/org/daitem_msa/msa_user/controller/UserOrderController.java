package org.daitem_msa.msa_user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_user.entity.User;
import org.daitem_msa.msa_user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문 회원 connection controller", description = "order -> user 로 보낸다")
public class UserOrderController {
    private final UserService userService;

    /**
     * 주문 - 회원 조회 endpoint(로그인 아이디)
     * @PathVariable : id = loginId
     * */
    @GetMapping("/api/v1/user-order/login-id/{id}")
    public User getUser(@PathVariable String id) {
        User user = userService.connectToOrderFindByLoginId(id);
        return user;
    }

    /**
     * 주문 - 회원 조회 endpoint (회원 아이디)
     * @PathVariable : id = loginId
     * */
    @GetMapping("/api/v1/user-order/id/{id}")
    public User getUser(@PathVariable long id) {
        User user = userService.connectToOrderFindById(id);
        return user;
    }
}
