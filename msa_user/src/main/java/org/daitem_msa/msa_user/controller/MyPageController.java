package org.daitem_msa.msa_user.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_user.common.CommonResponse;
import org.daitem_msa.msa_user.dto.MyPageUpdateDto;
import org.daitem_msa.msa_user.dto.UserDto;
import org.daitem_msa.msa_user.entity.User;
import org.daitem_msa.msa_user.service.MyPageService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "마이페이지", description = "마이페이지 API : 주문, 회원가입 시 등록한 정보 select, update 컨트롤러")
public class MyPageController {

    private final MyPageService myPageService;

    // 회원 정보 수정 (주소, 전화번호, 비밀번호)

    /**
     * 로그인 한 회원 정보 조회
     * @param user 회원 정보 (Security Context)
     * @return 회원 정보 DTO
     */
    @GetMapping("/api/v1/my-page")
    //@PreAuthorize("hasRole('ROLE_NORMAL')")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    public CommonResponse<UserDto> getUserMyPage(@AuthenticationPrincipal User user) {
        // header 에 저장되어있는 토큰과 redis 에 저장되어 있는 토큰 비교
        Long userId = user.getUserId();
        System.out.println(userId);
        return CommonResponse.ok(myPageService.getUserMyPage(user.getUserId()));
    }

    /**
     * 회원 정보 변경
     * @param user 회원 정보 (Security Context)
     * @return MemberMypageUpdateDto
     */
    @PutMapping("/api/v1/my-page")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
//    @PreAuthorizeorize("hasRole('NORMAL')")
    @ResponseBody
    public CommonResponse<?> updateMemberDetails(@AuthenticationPrincipal User user,
                                              @RequestBody MyPageUpdateDto updateDto){
        updateDto.setUserId(user.getUserId());
        try{
            myPageService.updateMemberDetails(updateDto);
            return new CommonResponse<>(200, "회원정보 변경에 성공했습니다.");
        }catch (Exception e) {
            return new CommonResponse<>(500, e.getMessage());
        }
    }


}
