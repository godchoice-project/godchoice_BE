package com.team03.godchoice.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GatherPostUpdateDto {
    private String imgId;
    private String category;
    private String date;
    private int number;
    private String kakaoLink;
    private String sex;
    private int startAge;
    private int endAge;
    private String title;
    private String content;
    private String postLink;
    private String postAddress;
}
