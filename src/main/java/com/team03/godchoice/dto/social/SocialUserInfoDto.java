package com.team03.godchoice.dto.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SocialUserInfoDto {
    private Long id;
    private String nickname;
    private String email;
    private String userImgUrl;
}
