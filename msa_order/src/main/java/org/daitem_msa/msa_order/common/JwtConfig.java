package org.daitem_msa.msa_order.common;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private long expirationTime = 30 * 60 * 1000L;


    /** 토큰 발급자 (issuer) */
    private final static String PAYLOAD_ISSUER = "iss";
    /** 토큰 제목 (subject) */
    private final static String PAYLOAD_SUBJECT = "sub";
    /** 토큰 대상자 (audience) */
    private final static String PAYLOAD_AUDIENCE = "aud";
    /** 토큰 만료 시간 (expiration time) */
    private final static String PAYLOAD_EXPIRATION_TIME = "exp";
    /** 토큰 활성 시간 (not before) */
    private final static String PAYLOAD_NOT_BEFORE = "nbf";
    /** 토큰 발급 시간 (issued at) */
    private final static String PAYLOAD_ISSUED_AT = "iat";
    /** 토큰 고유 식별자 (JWT ID) */
    private final static String PAYLOAD_JWT_ID = "jti";

    public TokenBuilder builder() {
        return new TokenBuilder();
    }

    public TokenParser parser(String token) {
        return new TokenParser(token);
    }

    /**
     * 토큰 생성<br />
     * 토큰 만료 시간을 설정하지 않으면 기본 값은 jwt.expiration-time 값을 읽는다. (기본 값: 10분)
     *
     * @see #createToken(Map, Long)
     * @param payload 토큰에 담을 정보 (body)
     * @return String JSON Web Token
     */
    public String createToken(Map<String, Object> payload) {
        return this.createToken(payload, expirationTime);
    }


    /**
     * 토큰 생성
     *
     * @param payload 토큰에 담을 정보 (body)
     * @param expirationSeconds 토큰 만료 시간 (단위: 초)
     * @return String JSON Web Token
     */
    public String createToken(Map<String, Object> payload, Long expirationSeconds) {
        Date now = new Date();

        JWTClaimsSet.Builder jwtBuilder = new JWTClaimsSet.Builder()
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + expirationSeconds * 1000));

        // Payload 추가
        for (String key : payload.keySet())
            jwtBuilder.claim(key, payload.get(key));

        JWTClaimsSet claimsSet = jwtBuilder.build();

        // JWT 문자열 빌드 및 서명
        SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS384).build(), claimsSet);
        try {
            jwt.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            log.error("JWT 서명 암호키 길이 부족 (48자 이상): ", e);
            return null;
        }

        return jwt.serialize();
    }

    /**
     * JWT 토큰 빌더 클래스
     */
    public class TokenBuilder {

        private final Map<String, Object> payload = new HashMap<>();
        private Long expirationSeconds;

        /**
         * 만료 시간 설정
         *
         * @param expirationSeconds 만료 시간 (단위: 초)
         * @return TokenBuilder (Method Chaining)
         * @see #expirationTime(Duration)
         */
        public TokenBuilder expirationTime(Long expirationSeconds) {
            this.expirationSeconds = expirationSeconds;
            return this;
        }

        /**
         * 만료 시간 설정
         *
         * @param duration 만료 시간
         * @return TokenBuilder (Method Chaining)
         * @see #expirationTime(Long)
         */
        public TokenBuilder expirationTime(Duration duration) {
            this.expirationSeconds = duration.toSeconds();
            return this;
        }

        /**
         * 토큰에 담을 정보 추가
         *
         * @param key 키 이름
         * @param value 키 값
         * @return TokenBuilder (Method Chaining)
         */
        public TokenBuilder claim(String key, Object value) {
            payload.put(key, value);
            return this;
        }

        /**
         * 토큰 생성
         *
         * @return String JSON Web Token
         */
        public String build() {
            if (expirationSeconds != null)
                return createToken(payload, expirationSeconds);
            else
                return createToken(payload);
        }

    }

    /**
     * JWT 토큰 파서 클래스
     */
    public class TokenParser {

        private final String token;

        /**
         * JWT 토큰 파싱에 필요한 생성자
         * @param token JSON Web Token
         */
        public TokenParser(String token) {
            if (!hasText(token))
                throw new JwtParseException("토큰이 존재하지 않습니다.");

            this.token = token;
        }

        /**
         * JWT 토큰 검증
         *
         * @return TokenClaims 토큰 정보가 담긴 TokenClaims 객체
         * @throws JwtParseException 토큰 파싱에 실패했을 때
         * @throws JwtSignatureException 토큰 서명 검증에 실패했을 때
         * @throws JwtExpiredException 토큰이 만료되었을 때
         */
        public TokenClaims verify() throws JwtParseException, JwtSignatureException, JwtExpiredException {
            if (this.token == null)
                throw new JwtParseException("토큰 정보가 없습니다.");

            Date now = new Date();
            SignedJWT signedJWT;

            try {
                signedJWT = SignedJWT.parse(this.token);
            } catch (ParseException e) {
                log.warn("JWT 토큰 파싱 실패: {}, token={}", e.getMessage(), this.token);
                throw new JwtParseException("토큰 파싱에 실패했습니다.");
            }

            // 서명 길이 검증 (HS384 알고리즘의 경우 64자)
            int signatureLength = signedJWT.getSignature().toString().length();
            if (signatureLength != 64) {
                log.warn("JWT 서명 Base64 길이 불일치: 예상=64, 실제={}", signatureLength);
                throw new JwtSignatureException("토큰 서명 길이 검증에 실패했습니다.");
            }

            // 토큰 서명 검증
            try {
                if (!signedJWT.verify(new MACVerifier(secretKey)))
                    throw new JwtSignatureException("토큰 서명 검증에 실패했습니다.");
            } catch (IllegalStateException e) {
                log.warn("JWT 토큰이 서명되지 않음: {}", e.getMessage());
                throw new JwtSignatureException("토큰이 서명되지 않았습니다.");
            } catch (JOSEException e) {
                log.warn("JWT 토큰 서명 검증 실패: {}", e.getMessage());
                throw new JwtSignatureException(e.getMessage());
            }

            JWTClaimsSet jwtClaimsSet;
            try {
                jwtClaimsSet = signedJWT.getJWTClaimsSet();

                // 토큰 만료 검증
                Date expirationTime = jwtClaimsSet.getExpirationTime();
                if (expirationTime == null)
                    throw new JwtExpiredException("토큰 만료시간 정보가 없습니다.");

                if (now.after(expirationTime))
                    throw new JwtExpiredException("토큰이 만료되었습니다.");
            } catch (ParseException e) {
                log.warn("JWT 토큰 Payload 파싱 실패: {}", e.getMessage());
                throw new JwtParseException("토큰 Payload 파싱에 실패했습니다.");
            }

            return new TokenClaims(jwtClaimsSet);
        }
    }

    /**
     * JWT 토큰 Payload 정보를 담는 클래스
     */
    @Getter
    public static class TokenClaims {

        private final Map<String, Object> payload;

        private TokenClaims(JWTClaimsSet jwtClaimsSet) {
            this.payload = jwtClaimsSet.getClaims();
        }

    }

    @Getter
    public static abstract class JwtException extends RuntimeException {

        private final int code;

        public JwtException(int code, String message) {
            super(message);
            this.code = code;
        }

    }

    public static class JwtParseException extends JwtException {
        public JwtParseException(String message) {
            super(HttpStatus.BAD_REQUEST.value(), message);
        }
    }

    public static class JwtSignatureException extends JwtException {
        public JwtSignatureException(String message) {
            super(HttpStatus.UNAUTHORIZED.value(), message);
        }
    }

    public static class JwtExpiredException extends JwtException {
        public JwtExpiredException(String message) {
            super(HttpStatus.UNAUTHORIZED.value(), message);
        }
    }



}
