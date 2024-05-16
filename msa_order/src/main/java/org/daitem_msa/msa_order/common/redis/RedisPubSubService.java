package org.daitem_msa.msa_order.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.common.redis.dto.MessageDto;
import org.daitem_msa.msa_order.dto.NewOrderSaveDto;
import org.daitem_msa.msa_order.entity.Item;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPubSubService {
    private final RedisMessageListenerContainer container;
    private final RedisPublisher publisher;
    private final RedisSubscribeListener listener;
    private final RedisTemplate<String, Item> redisTemplate;

    // 메세지 보내기
    public void sendMessage(String channel, MessageDto message) {
        container.addMessageListener(listener, new ChannelTopic(channel));
        publisher.publish(new ChannelTopic(channel), message);
    }

    // 매세지 취소
    public void canceledMessage(String channel) {
        container.removeMessageListener(listener);
        log.info("취소한 채널? {}", channel);
    }

    /** 주문 생성 메세지 생성
     *
     * */
    public void makeOrderMessage(MessageDto message, NewOrderSaveDto dto) {
        Item item = redisTemplate.opsForValue().get(dto.getItemId());
    }
}
