package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/likes/{kind}/{postId}")
    public GlobalResDto createPostLike(@PathVariable Long postId,
                                       @PathVariable String kind,
                                       @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postLikeService.createPostLike(postId, kind, userDetails);
    }
}
