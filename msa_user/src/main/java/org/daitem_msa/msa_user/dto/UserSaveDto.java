package org.daitem_msa.msa_user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.daitem_msa.msa_user.enumset.UserRoles;

@Getter
@Setter
public class UserSaveDto {
    private Long id;
    @NotEmpty(message = "아이디를 입력해 주세요.")
    @NotNull(message = "아이디를 입력해 주세요.")
    private String loginId;

    //@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
    private String userName;
    private UserRoles userRole;
    private String email;
    private String address1;
    private String address2;
    private String address3;
    private boolean isValid = false;
    private String phone;
}
