package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.service.SearchPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;


@RestController
@RequiredArgsConstructor
public class SearchPostController {

    private final SearchPostService searchPostService;

    @GetMapping("/allposts/{main}/{tag}/{progress}/{sort}/{search}/{page}")
    public GlobalResDto<?> searchPost(@PathVariable String main,
                                      @PathVariable String tag,
                                      @PathVariable String progress,
                                      @PathVariable String sort,
                                      @PathVariable String search,
                                      @PageableDefault(size = 10) Pageable pageable) {
        return searchPostService.searchPost(main, tag, progress, sort, search, pageable);
    }
}
