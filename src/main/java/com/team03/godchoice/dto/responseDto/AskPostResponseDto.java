package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostImg;
import lombok.Getter;

import java.util.List;

@Getter
public class AskPostResponseDto {

    private Long askPostId;

    private String title;

    private String content;

    private List<AskPostImg> askPostImgList;

    public AskPostResponseDto(AskPost askPost){
        this.askPostId=askPost.getAskPostId();
        this.title=askPost.getTitle();
        this.content=askPost.getContent();
        this.askPostImgList=askPost.getAskPostImg();
    }
}
