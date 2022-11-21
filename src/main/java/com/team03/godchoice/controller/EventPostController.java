package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.eventpostDto.EventPostPutReqDto;
import com.team03.godchoice.dto.requestDto.eventpostDto.EventPostReqDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.EventPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/eventposts")
public class EventPostController {

    private final EventPostService eventPostService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public GlobalResDto<?> createEventPost(@RequestPart(required = false) EventPostReqDto eventPostReqDto,
                                           @RequestPart(required = false) List<MultipartFile> multipartFile,
                                           @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return eventPostService.createEventPost(userDetails, eventPostReqDto, multipartFile);
    }

    @PutMapping("/{postId}")
    public GlobalResDto<?> putEventPost(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable Long postId,
                                        @RequestPart(required = false) EventPostPutReqDto eventPostPutReqDto,
                                        @RequestPart(required = false) List<MultipartFile> multipartFile) throws IOException {
        return eventPostService.putEventPost(userDetails, postId, eventPostPutReqDto, multipartFile);
    }

    @DeleteMapping("/{postId}")
    public GlobalResDto<?> deleteEventPost(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long postId) {
        return eventPostService.deleteEventPost(userDetails, postId);
    }

    @GetMapping("/{postId}")
    public GlobalResDto<?> getOneEventPost(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable Long postId,
                                                      HttpServletRequest req, HttpServletResponse res) {
        return eventPostService.getOneEventPost(userDetails, postId, req, res);
    }
}
