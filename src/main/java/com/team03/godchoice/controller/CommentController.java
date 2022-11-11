package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.CommentRequestDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("{postId}/comment")
    public GlobalResDto createComment(@PathVariable Long postId,
                                      @RequestBody CommentRequestDto commentRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails.getAccount());
    }


    @DeleteMapping("/comment/{commentId}")
    public GlobalResDto deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getAccount());
    }

}
