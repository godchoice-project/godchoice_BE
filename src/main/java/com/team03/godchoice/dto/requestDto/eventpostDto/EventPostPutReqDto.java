package com.team03.godchoice.dto.requestDto.eventpostDto;

import com.team03.godchoice.enumclass.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class EventPostPutReqDto {
    private String imgId;

    @NotBlank(message = "카테고리를 입력헤주세요")
    private Category category;

    @NotBlank(message = "날짜를 입력헤주세요")
    private String startPeriod;

    @NotBlank(message = "날짜를 입력해주세요")
    private String endPeriod;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력헤주세요")
    private String content;

    private String postLink;

    @NotBlank(message = "주소를 입력해주세요")
    private String postAddress;
}
