package com.team03.godchoice.dto.social;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleUserInfoDto {


    private String googleId;
    private String nickname;
    private String userEmail;
    private String profileUrl;

    @Builder
    public GoogleUserInfoDto(String googleId, String nickname, String userEmail) {
        this.googleId = googleId;
        this.nickname = nickname;
        this.userEmail = userEmail;
    }
}
