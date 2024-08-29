package com.apple.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private String userID;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userBirth;
    private String userNickname;
    private String userZonecode;
    private String userAddress;
    private String userAddressDetail;
}
