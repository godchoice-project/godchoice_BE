package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.GatherPostRequestDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.GatherPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gatherposts")
public class GatherPostController {

    private final GatherPostService gatherPostService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public GlobalResDto<?> createGatherPost(@RequestPart(required = false) GatherPostRequestDto gatherPostDto,
                                            @RequestPart(required = false) List<MultipartFile> multipartFile,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return gatherPostService.createGather(gatherPostDto,multipartFile,userDetails);
    }

    @GetMapping("/{gatherPostId}")
    public GlobalResDto<?> getGatherPostDetail(@PathVariable Long gatherPostId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return gatherPostService.getGather(gatherPostId,userDetails);
    }

}
