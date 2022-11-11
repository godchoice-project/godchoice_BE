package com.team03.godchoice.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GatherPostRequestDto {

    private String category;
    private String date;
    private int number;
    private String kakaoLink;
    private String sex;
    private int statAge;
    private int endAge;
    private String title;
    private String content;
    private String postLink;
    private String postAddress;
    private List<String> imageList;

}
