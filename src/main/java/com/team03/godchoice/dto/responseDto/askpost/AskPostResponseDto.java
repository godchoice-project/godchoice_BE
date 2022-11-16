package com.team03.godchoice.dto.responseDto.askpost;

import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostImg;
import com.team03.godchoice.dto.responseDto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AskPostResponseDto {

    private Long postId;
    private String title;
    private String content;
    private List<AskPostImg> askPostImgList;
    private List<CommentDto> commentDtoList;

    public AskPostResponseDto(AskPost askPost, List<AskPostImg> askPostImgList, List<CommentDto> commentDtoList){

        this.postId=askPost.getAskPostId();
        this.title=askPost.getTitle();
        this.content=askPost.getContent();
        this.askPostImgList=askPostImgList;
        this.commentDtoList=commentDtoList;
    }
}
