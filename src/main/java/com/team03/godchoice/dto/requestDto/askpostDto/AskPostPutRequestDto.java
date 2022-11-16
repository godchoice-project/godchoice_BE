package com.team03.godchoice.dto.requestDto.askpostDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AskPostPutRequestDto {

    private String title;

    private String content;

    private String postAddress;

    private String imgId;
}
