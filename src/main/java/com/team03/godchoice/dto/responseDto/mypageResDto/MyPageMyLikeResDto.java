package com.team03.godchoice.dto.responsedto.mypageResDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageMyLikeResDto {
    private List<MyPagePostResDto> eventPost;
    private List<MyPagePostResDto> gatherPost;
}