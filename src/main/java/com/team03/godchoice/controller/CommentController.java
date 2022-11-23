package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.CommentRequestDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
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

    @GetMapping("/{postId}/{kind}")
    public GlobalResDto<?> getComment(@PathVariable Long postId,
                                      @PathVariable String kind,
                                      @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.getComment(postId, kind);
    }

    @DeleteMapping("/{postId}/{commentId}/{kind}")
    public GlobalResDto<?> deleteComment(@PathVariable Long postId,
                                         @PathVariable Long commentId,
                                         @PathVariable String kind,
                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(postId,commentId, kind, userDetails.getAccount());
    }

}
