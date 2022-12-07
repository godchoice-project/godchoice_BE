package com.team03.godchoice.dto.requestDto.eventpostDto;

import com.team03.godchoice.enumclass.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class EventPostReqDto {
    @ApiModelProperty(example = "전시회")
    @NotBlank(message = "카테고리를 입력해주세요")
    private Category category;

    @ApiModelProperty(example = "2022-11-17")
    @NotBlank(message = "날짜를 입력해수세요")
    private String startPeriod;

    @ApiModelProperty(example = "2022-11-18")
    @NotBlank(message = "날짜를 입력해주세요")
    private String endPeriod;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String postLink;

    @ApiModelProperty(example = "경기도 ㅁㄴㅇㄹㅁㅁㅇ")
    @NotBlank(message = "주소를 입력해주세요")
    private String postAddress;
}
