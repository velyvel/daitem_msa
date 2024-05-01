package org.daitem_msa.msa_product.common.security;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
//질문 ? 다른 모듈로 요청하는데 base package 나 url 설정이 궁금하다. 81 포트에서 80 포트로
@EnableFeignClients(basePackages = {"org.daitem_msa.msa_product.common.security"})
public class OpenFeignConfig {
}
