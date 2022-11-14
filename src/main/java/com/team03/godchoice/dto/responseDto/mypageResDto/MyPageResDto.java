package com.team03.godchoice.dto.responseDto.mypageResDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResDto {
    private MyPageUserResDto user;
    private MyPageMyPostResDto myPost;
//    private MyPageMyLikeResDto myLike;
}
