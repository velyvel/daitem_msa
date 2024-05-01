package org.daitem_msa.msa_order.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userFeignClient.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));

//        return userRepository.findByLoginId(username)
//                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));
    }
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        return userFeignClient.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

}
