package com.team03.godchoice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.socialLogin.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/signup")
public class SocialLoginController {

    private final SocialGoogleService socialGoogleService;
    private final SocialGithubService socialGithubService;
    private final SocialKakaoService socialKakaoService;
    private final SocialNaverService2 socialNaverService;

    @GetMapping("/kakao")
    public GlobalResDto<?> kakaoLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialKakaoService.kakaoLogin(code, response);
    }

    @PostMapping("/kakao")
    public GlobalResDto<?> kakaoLogout(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return socialKakaoService.logoutKakao(userDetails.getMember());
    }

    @GetMapping("/github")
    public GlobalResDto<?> githubLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialGithubService.githubLogin(code, response);
    }

    @GetMapping("/google")
    public GlobalResDto<?> googleLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialGoogleService.googleLogin(code, response);
    }

    @GetMapping("/naver")
    public GlobalResDto<?> naverLogin(
            @RequestParam(value = "code") String code, @RequestParam String state, HttpServletResponse response) throws JsonProcessingException {
        return socialNaverService.naverLogin(code, state, response);
    }
}
