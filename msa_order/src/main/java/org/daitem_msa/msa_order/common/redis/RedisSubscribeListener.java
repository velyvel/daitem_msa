package org.daitem_msa.msa_order.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_order.common.redis.dto.MessageDto;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscribeListener implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try{
            String publishMessage = redisTemplate.getStringSerializer()
                    .deserialize(message.getBody());
            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);

            log.info("레디스 보내는 이 : {}", messageDto.getSender());
            log.info("레디스 보내는 메세지 : {}", messageDto.getMessage());
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }
}
