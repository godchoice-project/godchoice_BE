package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.askpostDto.AskPostPutRequestDto;
import com.team03.godchoice.dto.requestDto.askpostDto.AskPostRequestDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.AskPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/askposts")
public class AskPostController {

    private final AskPostService askPostService;

    @PostMapping
    public GlobalResDto<?> createAskPost(@RequestPart(required = false) AskPostRequestDto askPostRequestDto,
                                         @RequestParam(required = false) List<MultipartFile> multipartFile,
                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{

        return askPostService.createAskPost(askPostRequestDto,multipartFile,userDetails);
    }

    @PutMapping("/{postId}")
    public GlobalResDto<?> updateAskPost(@RequestPart(required = false) AskPostPutRequestDto askPostPutRequestDto,
                                         @RequestPart(required = false) List<MultipartFile> multipartFile,
                                         @PathVariable Long postId,
                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{

        return askPostService.updateAskPost(postId,askPostPutRequestDto,multipartFile,userDetails);
    }

    // 삭제하기
    @DeleteMapping(value = "/{postId}")
    public GlobalResDto<?> deletePost(@PathVariable Long postId,
                                      @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return askPostService.deletePost(postId, userDetails);
    }

    // 상세 조회
    @GetMapping(value = "/{postId}")
    public GlobalResDto<?> getOneAskPost(@PathVariable Long postId,
                                                  @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  HttpServletRequest req, HttpServletResponse res) {
        return askPostService.getOneAskPost(postId,req,res);
    }

}
