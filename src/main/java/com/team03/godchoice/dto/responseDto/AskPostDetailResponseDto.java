package com.team03.godchoice.dto.responsedto;

import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostImg;
import lombok.Getter;

import java.util.List;

@Getter
public class AskPostDetailResponseDto {

    private Long postId;
    private String title;
    private String content;
    private List<AskPostImg> askPostImgList;
    private List<com.team03.godchoice.dto.responseDto.CommentDto> commentDtoList;

    public AskPostDetailResponseDto(AskPost askPost, List<AskPostImg> askPostImgList, List<com.team03.godchoice.dto.responseDto.CommentDto> commentDtoList){

        this.postId=askPost.getAskPostId();
        this.title=askPost.getTitle();
        this.content=askPost.getContent();
        this.askPostImgList=askPostImgList;
        this.commentDtoList=commentDtoList;
    }
}
