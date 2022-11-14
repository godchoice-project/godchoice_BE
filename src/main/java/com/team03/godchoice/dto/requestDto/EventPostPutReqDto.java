package com.team03.godchoice.dto.requestDto;

import com.team03.godchoice.domain.domainenum.Category;
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
