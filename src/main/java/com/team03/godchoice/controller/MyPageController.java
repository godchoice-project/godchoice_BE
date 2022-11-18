package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.MyPageReqDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @PutMapping( consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public GlobalResDto<?> changeUserInfo(@RequestPart(required = false) MyPageReqDto user,
                                          @RequestPart(required = false) MultipartFile multipartFile,
                                          @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return myPageService.changeUserInfo(user, multipartFile, userDetails);
    }

    @GetMapping("/user")
    public GlobalResDto<?> getUser(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return myPageService.getUser(userDetails);
    }

    @GetMapping("/mypost")
    public GlobalResDto<?> getMyPost(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return myPageService.getMyPost(userDetails);
    }

    @GetMapping("/mycomment")
    public GlobalResDto<?> getMyComment(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return myPageService.getMyComment(userDetails);
    }

    @GetMapping("/myscrap")
    public GlobalResDto<?> getMyScrap(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return myPageService.getMyScrap(userDetails);
    }
}
