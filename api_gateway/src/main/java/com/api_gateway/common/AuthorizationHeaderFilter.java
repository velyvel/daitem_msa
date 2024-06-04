package com.api_gateway.common;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import java.text.ParseException;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>{


  private final RedisUtil redisUtil;
  private final Environment env;
  public static class Config { }

  @Autowired
  public AuthorizationHeaderFilter(RedisUtil redisUtil, Environment env) {
    super(Config.class);
    this.redisUtil = redisUtil;
    this.env = env;
  }

  @Override
  public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
    return (((exchange, chain) -> {

      ServerHttpRequest request = exchange.getRequest();

      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        return webfluxError(exchange, "토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
      }

      String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String token = authorizationHeader.replace("Bearer ", "");

      String path = request.getURI().getPath();
      if ("/logout".equals(path)) {
        redisUtil.set(token, "accessToken", 60);
        return webfluxResponse(exchange, "로그아웃",HttpStatus.OK);
      }

      String uuid = isJwtValid(token, exchange);

      if (uuid == null) {
        return webfluxError(exchange, "토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED);
      }

      if (uuid.equals("로그아웃 한 회원입니다")) {
        return webfluxResponse(exchange, "로그아웃 한 회원입니다", HttpStatus.UNAUTHORIZED);
      }

      if ("/password".equals(path)) {
        redisUtil.set(token, "accessToken", 60);
      }

      ServerHttpRequest newRequest = request.mutate().header("UUID", uuid).build();

      return chain.filter(exchange.mutate().request(newRequest).build());
    }));
  }

  private String validateTokenAndGetUUID(String token,ServerWebExchange exchange) {
    byte[] decode = Base64.getDecoder().decode(env.getProperty("jwt.secret.key").getBytes());
    SecretKey secretKey = new SecretKeySpec(decode, 0, decode.length, "HmacSHA256");

    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(secretKey);

      if (!signedJWT.verify(verifier)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 검증에 실패했습니다");
      }

      JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
      if (redisUtil.isKeyDeleted(token)) {
        return "로그아웃 한 회원입니다";
      }
      return claims.getStringClaim("memberUUID");

    } catch (ParseException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 파싱에 실패했습니다", e);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 검증 중 오류가 발생했습니다", e);
    }
  }

  private String isJwtValid(String token, ServerWebExchange exchange) {
    String uuid = null;
    try {
      uuid = validateTokenAndGetUUID(token,exchange);
    } catch (Exception e) {
      throw new RuntimeException("회원의 정보가 잘못되었습니다");
    }
    return uuid;
  }

  // custom response
  /**
   * Mono -> reactive programming
   * spring webflux 와 같은 프로젝트에서 비동기식으로 데이터를 처리하기 위해 사용
   * */
  //
  private Mono<Void> webfluxResponse(ServerWebExchange exchange, String message, HttpStatus status) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(status);

    return response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(message.getBytes())));
  }

  // custom error
  // 오류 발생시 로그 기록용(비동기라 신호, 로그 남겨두어야 함
  private Mono<Void> webfluxError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);

    // 로그 기록
    log.error("webflux error = {}",error);
    return response.setComplete();
  }
}
