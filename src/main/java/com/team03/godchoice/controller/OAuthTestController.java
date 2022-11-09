package com.team03.godchoice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team03.godchoice.dto.responsedto.social.SocialResponseDto;
import com.team03.godchoice.service.socialLogin.SocialNaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/signup")
public class OAuthTestController {

    private final SocialNaverService socialNaverService;

    @GetMapping("/naver")
    public SocialResponseDto naverLogin(@RequestParam(value = "code") String code,
                                        @RequestParam String state,
                                        HttpServletResponse response) throws JsonProcessingException {
        return socialNaverService.naverLogin(code, state, response);
    }
}
