package com.team03.godchoice.dto.userinfo;

import lombok.Getter;

@Getter
public class GithubReqDto {

    private String access_token;
    private String token_type;
    private String scope;
}