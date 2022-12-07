package com.team03.godchoice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.service.ReTokenService;
import com.team03.godchoice.service.socialLogin.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/signup")
public class SocialLoginController {

    private final SocialGoogleService socialGoogleService2;
    private final SocialGithubService socialGithubService2;
    private final SocialKakaoService socialKakaoService2;
    private final SocialNaverService3 socialNaverService3;
    private final ReTokenService reTokenService;

    @GetMapping("/kakao")
    public GlobalResDto<?> kakaoLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialKakaoService2.loginService(code, response);
    }

    @GetMapping("/github")
    public GlobalResDto<?> githubLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialGithubService2.loginService(code, response);
    }

    @GetMapping("/google")
    public GlobalResDto<?> googleLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialGoogleService2.loginService(code, response);
    }

    @GetMapping("/naver")
    public GlobalResDto<?> naverLogin(
            @RequestParam(value = "code") String code, @RequestParam String state, HttpServletResponse response) throws JsonProcessingException {
        return socialNaverService3.loginService(code, state, response);
    }

    @GetMapping("/issue/token")
    public GlobalResDto<?> issuedToken(HttpServletRequest request,HttpServletResponse response) {
        reTokenService.createNewToken(request.getHeader("Refresh_Token"),response);
        return GlobalResDto.success(null,"Success IssuedToken");
    }

}
