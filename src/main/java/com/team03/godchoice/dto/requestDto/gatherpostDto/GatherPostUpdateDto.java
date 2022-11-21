package com.team03.godchoice.dto.requestDto.gatherpostDto;

import com.team03.godchoice.domain.domainenum.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GatherPostUpdateDto {
    private String imgId;
    private Category category;
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