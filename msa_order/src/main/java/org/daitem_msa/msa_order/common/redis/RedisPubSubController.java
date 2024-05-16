package org.daitem_msa.msa_order.common.redis;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.common.redis.dto.MessageDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "메세지 테스트 컨트롤러", description = "redis pub-sub 테스트용 컨트롤러")
public class RedisPubSubController {
    private final RedisPubSubService redisPubSubService;

    @PostMapping("/api/v2/order/redis-pub-sub-send")
    public void sendMessage(@RequestParam(required = true) String channel, @RequestBody MessageDto message) {
        log.info("레디스 채널은? = {}", channel);
        redisPubSubService.sendMessage(channel, message);
    }

    @PostMapping("/api/v2/order/redis-pub-sub-canceled")
    public void canceledMessage(@RequestParam(required = true) String channel) {
        redisPubSubService.canceledMessage(channel);
    }
}
