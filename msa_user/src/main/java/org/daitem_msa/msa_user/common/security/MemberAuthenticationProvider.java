package org.daitem_msa.msa_user.common.security;

import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_user.entity.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {

    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        // 아이디 없으면 UsernameNotFoundException 발생
        User user = (User) memberDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");

//        if (!user.isAccountNonExpired())
//            throw new AccountExpiredException("만료된 계정입니다. 관리자에게 문의하세요.");
//
//        if (!user.isCredentialsNonExpired())
//            throw new CredentialsExpiredException("비밀번호가 만료되었습니다. 비밀번호를 변경하고 로그인 해주세요.");

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
