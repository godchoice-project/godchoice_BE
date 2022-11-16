package com.team03.godchoice.controller;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.service.SearchPostService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchPostController {

    private final SearchPostService searchPostService;

    @ApiOperation(value = "검색", notes = "tag는 빈값일때 한칸띄어쓰기로 보내시면 오류가 생기지 않습니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "main",value = "글 구분(event/gather/ask)",defaultValue = "event"),
            @ApiImplicitParam(name = "tag",value = "지역태그",defaultValue = " "),
            @ApiImplicitParam(name = "progress",value = "글 상태(진행중/종료)",defaultValue = "진행중"),
            @ApiImplicitParam(name = "sort",value = "정렬기준(최신순/인기순)",defaultValue = "최신순"),
            @ApiImplicitParam(name = "search",value = "검색어")
    })
    @GetMapping("/allposts")
    public GlobalResDto<?> searchPost(@RequestParam(defaultValue = "event") String main,
                                      @RequestParam(required = false) List<String> tag,
                                      @RequestParam(defaultValue = "진행중") String progress,
                                      @RequestParam(defaultValue = "최신순") String sort,
                                      @RequestParam(required = false)  String search,
                                      @PageableDefault(size = 10) Pageable pageable,
                                      @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return searchPostService.searchPost(main, tag, progress, sort, search, pageable,userDetails);
    }
}
