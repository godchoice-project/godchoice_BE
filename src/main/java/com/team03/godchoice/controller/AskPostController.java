package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.AskPostPutRequestDto;
import com.team03.godchoice.dto.requestDto.AskPostRequestDto;
import com.team03.godchoice.dto.responseDto.AskPostDetailResponseDto;
import com.team03.godchoice.dto.responseDto.AskPostResponseDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.AskPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/askposts/{postId}")
    public GlobalResDto<?> updateAskPost(@RequestPart(required = false)AskPostPutRequestDto askPostPutRequestDto,
                                         @RequestPart(required = false) List<MultipartFile> multipartFile,
                                         @PathVariable Long postId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{

        return askPostService.updateAskPost(postId,askPostPutRequestDto,multipartFile,userDetails);
    }

    // 삭제하기
    @DeleteMapping(value = "/askposts/{postId}")
    public GlobalResDto<?> deletePost(@PathVariable Long postId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return askPostService.deletePost(postId, userDetails);
    }

    //전체 조회
    @GetMapping(value = "/allposts")
    private List<AskPostResponseDto> getAllAskPost(){
        return askPostService.getAllAskPost();
    }

    // 상세 조회
    @GetMapping(value = "/askposts/{postId}")
    public AskPostDetailResponseDto getOneAskPost(@PathVariable Long postId) {

        return askPostService.getOneAskPost(postId);
    }

}
