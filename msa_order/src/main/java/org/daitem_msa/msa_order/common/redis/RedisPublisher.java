package org.daitem_msa.msa_order.common.redis;

import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_order.common.redis.dto.MessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //Object
    public void publish(ChannelTopic topic, MessageDto dto) {
        redisTemplate.convertAndSend(topic.getTopic(), dto);
    }

    //String
    public void publish(ChannelTopic topic ,String data) {
        redisTemplate.convertAndSend(topic.getTopic(), data);
    }

}
