package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.CommentRequestDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("comments/{postId}")
    public GlobalResDto createComment(@PathVariable Long postId,
                                      @RequestBody CommentRequestDto commentRequestDto,
                                      @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails.getAccount());
    }


    @DeleteMapping("/comments/{postId}/{commentId}")
    public GlobalResDto deleteComment(@PathVariable Long commentId,
                                      @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getAccount());
    }

}
