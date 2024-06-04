package org.daitem_msa.msa_order.common.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class LuaScriptConfig {
  @Bean
  public RedisScript<Long> stockDecreaseScript() {
    // src/main/resources 에 스크립트 파일을 넣어주기 위해 사용한다
    ClassPathResource scriptSource = new ClassPathResource("stockDecrease.lua");
    return RedisScript.of(scriptSource, Long.class);
  }


}
