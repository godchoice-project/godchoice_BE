package com.team03.godchoice.dto.requestDto.eventpostDto;

import com.team03.godchoice.domain.domainenum.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventPostReqDto {
    @ApiModelProperty(example = "전시회")
    private Category category;
    @ApiModelProperty(example = "2022-11-17")
    private String startPeriod;
    @ApiModelProperty(example = "2022-11-18")
    private String endPeriod;
    private String title;
    private String content;
    private String postLink;
    @ApiModelProperty(example = "경기도 ㅁㄴㅇㄹㅁㅁㅇ")
    private String postAddress;
}
