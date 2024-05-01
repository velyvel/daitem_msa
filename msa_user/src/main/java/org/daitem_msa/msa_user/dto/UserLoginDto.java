package org.daitem_msa.msa_user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {
    @Schema(description = "로그인 ID")
    private String loginId;

    @Schema(description = "비밀번호")
    private String password;

}
