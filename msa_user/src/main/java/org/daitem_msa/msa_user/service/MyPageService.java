package org.daitem_msa.msa_user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daitem_msa.msa_user.dto.MyPageUpdateDto;
import org.daitem_msa.msa_user.dto.UserDto;
import org.daitem_msa.msa_user.entity.User;
import org.daitem_msa.msa_user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    // 회원정보 조회 메서드
    @Transactional
    public UserDto getUserMyPage(Long userId) {
        UserDto userDto = userRepository.searchUserBy(userId);
        if(userDto == null) {
            throw new RuntimeException("회원정보를 찾을 수 없습니다.");
        }
        return userDto;
    }

    //회원정보 수정 메서드
    @Transactional
    public void updateMemberDetails(MyPageUpdateDto updateDto) {

        // 회원 정보 수정 (주소, 전화번호, 비밀번호)
        User user = userRepository.findById(updateDto.getUserId())
                .orElseThrow(() -> new RuntimeException("회원이 조회되지 않습니다."));

        // 값이 바뀌지 않을 경우는 화면단에서 value 로 가지고 있는데, 이걸 서버쪽에서 처리하는 방법은 ?
        String originalPassword = user.getPassword();
        user.setPassword(user.getPassword());
        user.setAddress1(updateDto.getAddress1());
        user.setAddress2(updateDto.getAddress2());
        user.setAddress3(updateDto.getAddress3());
        user.setPhone(updateDto.getPhone());
        userRepository.save(user);

        // 비밀번호 변경할 경우 -> 모든 기기에서 로그아웃
        if(!originalPassword.equals(updateDto.getPassword())) {
            //로그아웃 처리
        }
    }
}
