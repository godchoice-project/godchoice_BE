package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.AskPostRequestDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.AskPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AskPostController {

    private final AskPostService askPostService;

    @PostMapping("/askposts")
    public GlobalResDto<?> createAskPost(@RequestPart(required = false)AskPostRequestDto askPostRequestDto,
                                      @RequestPart(required = false) List<MultipartFile> multipartFile,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{

        return askPostService.createAskPost(askPostRequestDto,multipartFile,userDetails);
    }

}
