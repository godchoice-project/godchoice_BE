package com.team03.godchoice.dto.requestDto.askpostDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AskPostPutRequestDto {

    @NotBlank(message = "제목을 입력헤주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String postAddress;

    private String postLink;

    private String imgId;
}
