package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.EventPostPutReqDto;
import com.team03.godchoice.dto.requestDto.EventPostReqDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.EventPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class EventPostController {

    private final EventPostService eventPostService;

    @PostMapping("/eventposts")
    public GlobalResDto<?> createEventPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestPart(required = false) EventPostReqDto eventPostReqDto,
                                           @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        return eventPostService.createEventPost(userDetails, eventPostReqDto, multipartFiles);
    }

    @PutMapping("/eventposts/{postId}")
    public GlobalResDto<?> putEventPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestParam Long postId,
                                        @RequestPart(required = false) EventPostPutReqDto eventPostPutReqDto,
                                        @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        return eventPostService.putEventPost(userDetails,postId,eventPostPutReqDto,multipartFiles);
    }

    @DeleteMapping("/eventposts/{postId}")
    public GlobalResDto<?> deleteEventPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestParam Long postId) {
        return eventPostService.deleteEventPost(userDetails, postId);
    }
}
