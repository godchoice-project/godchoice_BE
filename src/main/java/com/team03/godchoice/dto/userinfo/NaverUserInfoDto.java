package com.team03.godchoice.dto.userinfo;

import lombok.*;

@Data
@AllArgsConstructor
public class NaverUserInfoDto {

    private String SocialId;
    private String nickName;
    private String userEmail;
    private String userImgUrl;
    private String accessToken;
    private String refreshToken;

    @Builder
    public NaverUserInfoDto(String naverId, String nickName, String userEmail, String access_token, String refresh_token) {
        this.SocialId = naverId;
        this.nickName = nickName;
        this.userEmail = userEmail;
        this.accessToken = access_token;
        this.refreshToken = refresh_token;
    }
}