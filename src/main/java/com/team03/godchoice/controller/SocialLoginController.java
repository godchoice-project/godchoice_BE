package com.team03.godchoice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.social.responseDto.SocialResponseDto;
import com.team03.godchoice.service.socialLogin.SocialGithubService;
import com.team03.godchoice.service.socialLogin.SocialGoogleService;
import com.team03.godchoice.service.socialLogin.SocialKakaoService;
import com.team03.godchoice.service.socialLogin.SocialNaverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/signup")
public class SocialLoginController {


    private final SocialGoogleService socialGoogleService;
    private final SocialGithubService socialGithubService;
    private final SocialKakaoService socialKakaoService;
    private final SocialNaverService socialNaverService;

    @GetMapping("/kakao")
    public GlobalResDto<?> kakaoLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialKakaoService.kakaoLogin(code, response);
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
    public SocialResponseDto naverLogin(@RequestParam(value = "code") String code,
                                        @RequestParam String state,
                                        HttpServletResponse response) throws JsonProcessingException {
        return socialNaverService.naverLogin(code, state, response);
    }
}
