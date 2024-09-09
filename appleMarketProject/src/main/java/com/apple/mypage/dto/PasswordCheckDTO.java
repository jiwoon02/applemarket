package com.apple.mypage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordCheckDTO {
    private Long userNo;
    private String userPwd;
}