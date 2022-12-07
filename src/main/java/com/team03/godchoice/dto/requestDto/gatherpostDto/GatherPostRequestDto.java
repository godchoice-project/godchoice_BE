package com.team03.godchoice.dto.requestDto.gatherpostDto;

import com.team03.godchoice.enumclass.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GatherPostRequestDto {
    @NotBlank(message = "카테고리를 입력해주세요")
    private Category category;

    @NotBlank(message = "날짜를 입력해주세요")
    private String date;

    @NotBlank(message = "인원을 입력해주세요")
    private int number;

    @NotBlank(message = "연락할 방법을 입력해주세요")
    private String kakaoLink;

    @NotBlank(message = "성별을 선택해주세요")
    private String sex;

    @NotBlank(message = "나이를 입력해주세요")
    private int startAge;

    @NotBlank(message = "날짜를 입력해주세요")
    private int endAge;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String postLink;

    private String postAddress;
}
