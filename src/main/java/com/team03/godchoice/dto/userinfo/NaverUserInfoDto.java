package com.team03.godchoice.dto.userinfo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class NaverUserInfoDto {

//    private String SocialId;
    private String email;
    private String userName;
    private String userImgUrl;
    private String sex;
    private String accessToken;
    private String refreshToken;

    @Builder
    public NaverUserInfoDto(String SocialId, String nickname, String userEmail, String accessToken,  String refreshToken) {
//        this.SocialId = SocialId;
//        this.nickuserName = nickuserName;
//        this.userEmail = userEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}