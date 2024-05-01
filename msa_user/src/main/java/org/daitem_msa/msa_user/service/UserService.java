package org.daitem_msa.msa_user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_user.common.EmailConfig;
import org.daitem_msa.msa_user.common.JwtConfig;
import org.daitem_msa.msa_user.dto.LoginTokenDto;
import org.daitem_msa.msa_user.dto.UserLoginDto;
import org.daitem_msa.msa_user.dto.UserSaveDto;
import org.daitem_msa.msa_user.entity.User;
import org.daitem_msa.msa_user.enumset.UserRoles;
import org.daitem_msa.msa_user.enumset.YN;
import org.daitem_msa.msa_user.repository.UserRepository;
import org.daitem_msa.msa_user.common.security.MemberAuthenticationProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfig emailConfig;


    private final MemberAuthenticationProvider memberAuthenticationProvider;
    private final JwtConfig jwtConfig;
    private final RedisTemplate<String, String> redisTemplate;


    @Transactional
    public void userAdd(UserSaveDto dto) {
        /**
         * 각각 validation 화요일에 작성한다.
         * */
        String encodePassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .loginId(dto.getLoginId())
                .password(encodePassword)
                .userName(dto.getUserName())
                .userRole(UserRoles.NONE)
                .email(dto.getEmail())
                .address1(dto.getAddress1())
                .address2(dto.getAddress2())
                .address3(dto.getAddress3())
                .isValid(YN.N)
                .build();
        userRepository.save(user);

        // 이메일 인증 추가 필요

        // 이메일 인증 시 질문 ?
        String to = dto.getEmail();
        if (to != null) {
            emailConfig.sendMail(to);
        } else {
            // 이메일이 null인 경우 처리
            System.out.println("이메일이 없습니다.");
        }
    }

    public void checkVerifyCode(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        user.setUserRole(UserRoles.NORMAL);
        user.setIsValid(YN.Y);
    }

    @Transactional
    public LoginTokenDto userLoginProcess(UserLoginDto requestDto) {

        if (!hasText(requestDto.getLoginId()) || !hasText(requestDto.getPassword())) {
            throw new RuntimeException("로그인 정보를 입력해주세요.");
        }

        try {
            Authentication authentication = memberAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword()));

            User user = (User) authentication.getPrincipal();
            user.setLastLoginAt(LocalDateTime.now()); // 마지막 로그인 시간 업데이트
            userRepository.save(user); // DB에 사용자 정보 업데이트

            String accessToken = jwtConfig.builder()
                    .claim("userId", user.getUserId())
                    .claim("loginId", user.getLoginId())
                    .claim("role", user.getUserRole().getRole())
                    .expirationTime(Duration.ofHours(1).toSeconds())
                    .build();

            String refreshToken = jwtConfig.builder()
                    .claim("userId", user.getUserId())
                    .claim("loginId", user.getLoginId())
                    .claim("role", user.getUserRole().getRole())
                    .expirationTime(Duration.ofDays(7).toMillis())
                    .build();

            // Redis에 accessToken과 refreshToken 저장
            redisTemplate.opsForValue().set("auth:accessToken:" + user.getUserId(), accessToken, Duration.ofHours(1));
            redisTemplate.opsForValue().set("auth:refreshToken:" + user.getUserId(), refreshToken, Duration.ofDays(7));

            LoginTokenDto loginTokenDto = new LoginTokenDto();
            loginTokenDto.setAccessToken(accessToken);
            loginTokenDto.setRefreshToken(refreshToken);

            return loginTokenDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("토큰 생성 중 오류가 발생했습니다.");
        }
    }

    public User connectToOrderFindByLoginId(String loginId) {
       return userRepository.findByLoginId(loginId)
               .orElseThrow(() -> new RuntimeException("주문에서 넘어온 회원 fail"));
    }

    public User connectToOrderFindById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문에서 넘어온 회원 fail"));
    }
}
