package org.daitem_msa.msa_user.dto;

import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_user.enumset.UserRoles;
import java.time.LocalDateTime;


@Getter
@Setter
public class UserDto {

    private Long userId;

    private String loginId;

    @Convert(converter = UserRoles.UserRolesConverter.class)
    private UserRoles userRole;

    private String userName;

    private String email;

    private String address1;

    private String address2;

    private String address3;

    private LocalDateTime lastLoginAt;

    private LocalDateTime verifyIdentityRequestAt;

    private LocalDateTime createdAt;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    //@Length(min=8, max=20, message="비밀번호는 8~20자의 영문/특문/숫자 조합으로 띄어쓰기 없이 입력해 주세요.")
    private String password;
}
