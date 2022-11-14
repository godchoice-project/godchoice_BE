package com.team03.godchoice.dto.responseDto.mypageResDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageMyPostResDto {
    private List<MyPagePostResDto> eventPost;
    private List<MyPagePostResDto> gatherPost;
    private List<MyPagePostResDto> askPost;
}
