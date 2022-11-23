package com.team03.godchoice.dto.requestDto.eventpostDto;

import com.team03.godchoice.enumclass.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventPostPutReqDto {
    private String imgId;
    private Category category;
    private String startPeriod;
    private String endPeriod;
    private String title;
    private String content;
    private String postLink;
    private String postAddress;
}
