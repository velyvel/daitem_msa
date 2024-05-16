package org.daitem_msa.msa_order.forlock;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.CommonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "데이터베이스 락 test", description = "for test api")
public class LockController {
    private final ItemService itemService;

    @PostMapping("/api/v1/optimistic/request")
    public CommonResponse<?> forOptimistic(@RequestBody LockDto dto) {
        try {
            itemService.forOptimistic(dto);
            return CommonResponse.ok("주문 요청 성공");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @PostMapping("/api/v1/optimistic/request")
    public CommonResponse<?> forPessimistic(@RequestBody LockDto dto) {
        try {
            itemService.forPessimistic(dto);
            return CommonResponse.ok("주문 요청 성공");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

}
