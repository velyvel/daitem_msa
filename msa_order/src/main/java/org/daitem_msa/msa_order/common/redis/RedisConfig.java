package org.daitem_msa.msa_order.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Slf4j
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    // redis connection
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        System.out.println("레디스 커넥션");
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    // redis template
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // 기본 직렬화( JSON 형식) Object <=> JSON
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());

        // Key 직렬화 (String 형식)
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // value 직렬화 (JSON 형식)
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 해시 데이터 구조 직렬화 (JSON 형식)
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    // JPA 활용 transactionManager 사용
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    // pub/sub 메세지처리 Listener
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        return container;
    }

}
