package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.SearchPostService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class SearchPostController {

    private final SearchPostService searchPostService;

    @GetMapping("/allposts")
    public GlobalResDto<?> searchPost(@RequestParam(defaultValue = "event") String main,
                                      @RequestParam(required = false) List<String> tag,
                                      @RequestParam(defaultValue = "진행중") String progress,
                                      @RequestParam(defaultValue = "최신순") String sort,
                                      @RequestParam String search,
                                      @PageableDefault(size = 10) Pageable pageable,
                                      @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return searchPostService.searchPost(main, tag, progress, sort, search, pageable,userDetails);
    }
}
