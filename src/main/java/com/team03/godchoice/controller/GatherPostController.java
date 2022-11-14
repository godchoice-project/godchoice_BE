package com.team03.godchoice.controller;

import com.team03.godchoice.domain.domainenum.Category;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.GatherPostRequestDto;
import com.team03.godchoice.dto.requestDto.GatherPostUpdateDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.GatherPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

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
                                            Category category) throws IOException {
        return gatherPostService.createGather(gatherPostDto,multipartFile, category);
    }

    @PutMapping("/{postId}")
    public GlobalResDto<?> putGatherPost(@PathVariable Long postId,
                                         @RequestPart GatherPostUpdateDto gatherPostDto,
                                         Category category,
                                         @RequestPart List<MultipartFile> multipartFile) throws IOException {
        return gatherPostService.updateGatherPost(postId, gatherPostDto, multipartFile, category);
    }

    @DeleteMapping("/{postId}")
    public GlobalResDto<?> deleteGatherPost(@PathVariable Long postId) {
        return gatherPostService.deleteGatherPost(postId);
    }

//    @GetMapping("{postId}")
//    public GlobalResDto<?> gatGatherPsot(@PathVariable Long postId) {
//        return gatherPostService.getGatherPost(postId);
//    }
}
