package com.team03.godchoice.dto.social;

import lombok.*;

@Data
@Getter
public class NaverUserInfoDto {
    private String SocialId;
    private String nickName;
    private String userEmail;
    private String userImgUrl;
    private String accessToken;
    private String refreshToken;

    @Builder
    public NaverUserInfoDto(String SocialId, String nickName, String userEmail, String userImgUrl,String access_token, String refresh_token) {
        this.SocialId = SocialId;
        this.nickName = nickName;
        this.userEmail = userEmail;
        this.userImgUrl = userImgUrl;
        this.accessToken = access_token;
        this.refreshToken = refresh_token;
    }
}