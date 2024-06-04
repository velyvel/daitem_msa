package com.api_gateway.common;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

  private final RedisTemplate<String, Object> redisTemplate;

  // 블랙리스트 만들기
  public void set(String key, Object value, int time) {
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(value.getClass()));
    redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
  }

  // 블랙리스트 조회
  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  // 로그아웃 시 토큰 삭제
  public boolean isDelete(String key) {
    return Boolean.TRUE.equals(redisTemplate.delete(key));
  }

  // 블랙리스트 판별여부
  public boolean isKeyDeleted(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }
}
