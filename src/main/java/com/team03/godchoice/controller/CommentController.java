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
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{postId}/{kind}")
    public GlobalResDto<?> createComment(@PathVariable Long postId,
                                         @PathVariable String kind,
                                         @RequestBody CommentRequestDto commentRequestDto,
                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, kind, commentRequestDto, userDetails.getAccount());
    }


    @DeleteMapping("/{commentId}/{kind}")
    public GlobalResDto<?> deleteComment(@PathVariable Long commentId,
                                         @PathVariable String kind,
                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, kind, userDetails.getAccount());
    }

}
