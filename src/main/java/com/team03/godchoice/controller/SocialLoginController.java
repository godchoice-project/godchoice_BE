package com.team03.godchoice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.service.socialLogin.SocialGithubService;
import com.team03.godchoice.service.socialLogin.SocialKakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialKakaoService socialKakaoService;
    private final SocialGithubService socialGithubService;

    @GetMapping("/member/signup/kakao")
    public GlobalResDto<?> kakaoLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialKakaoService.kakaoLogin(code, response);
    }

    @GetMapping("/member/signup/github")
    public GlobalResDto<?> githubLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialGithubService.githubLogin(code, response);
    }
}
