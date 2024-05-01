package org.daitem_msa.msa_user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageUpdateDto {
    private Long userId;
    private String phone;
    private String password;
    private String address1;
    private String address2;
    private String address3;
}
