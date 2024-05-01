package org.daitem_msa.msa_user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginTokenDto {

    private String accessToken;
    private String refreshToken;

}
