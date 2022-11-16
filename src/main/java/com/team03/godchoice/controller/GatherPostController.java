package com.team03.godchoice.controller;

import com.team03.godchoice.domain.domainenum.Category;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.GatherPostRequestDto;
import com.team03.godchoice.dto.requestDto.GatherPostUpdateDto;
import com.team03.godchoice.dto.responseDto.GatherPostDetailResponseDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.GatherPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gatherposts")
public class GatherPostController {

    private final GatherPostService gatherPostService;

    //    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PostMapping
    public GlobalResDto<?> createGatherPost(@RequestPart(required = false) GatherPostRequestDto gatherPostDto,
                                            @RequestPart(required = false) List<MultipartFile> multipartFile,
                                            Category category,
                                            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return gatherPostService.createGather(gatherPostDto, multipartFile, category, userDetails);
    }

    @PutMapping("/{postId}")
    public GlobalResDto<?> putGatherPost(@PathVariable Long postId,
                                         @RequestPart GatherPostUpdateDto gatherPostDto,
                                         @RequestPart List<MultipartFile> multipartFile,
                                         Category category,
                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return gatherPostService.updateGatherPost(postId, gatherPostDto, multipartFile, category, userDetails);
    }

    @DeleteMapping("/{postId}")
    public GlobalResDto<?> deleteGatherPost(@PathVariable Long postId,
                                            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return gatherPostService.deleteGatherPost(postId, userDetails);
    }

    @GetMapping("/{postId}")
    public GatherPostDetailResponseDto getGatherPost(@PathVariable Long postId,
                                                     @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     HttpServletRequest req, HttpServletResponse res) {
        return gatherPostService.getGatherPost(postId, userDetails, req, res);
    }
}
