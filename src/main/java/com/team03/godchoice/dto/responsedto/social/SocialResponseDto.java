package com.team03.godchoice.dto.responsedto.social;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialResponseDto {
    private String email;
    private String userName;
    private String userImgUrl;
    private String sex;
    private String accessToken;
    private String refreshToken;

    public SocialResponseDto(String email, String userName, String userImgUrl, String sex, String accessToken, String refreshToken) {
        this.email = email;
        this.userName = userName;
        this.userImgUrl = userImgUrl;
        this.sex = sex;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
