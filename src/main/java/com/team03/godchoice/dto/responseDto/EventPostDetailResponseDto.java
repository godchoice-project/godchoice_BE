package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostImg;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostImg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostDetailResponseDto {

    private Long postId;
    private String title;
    private String content;
    private List<String> eventPostImgUrl;
    private List<CommentDto> commentDtoList;

    public EventPostDetailResponseDto(EventPost eventPost, List<String> eventPostImgUrl, List<CommentDto> commentDtoList){

        this.postId=eventPost.getEventPostId();
        this.title=eventPost.getTitle();
        this.content=eventPost.getContent();
        this.eventPostImgUrl=eventPostImgUrl;
        this.commentDtoList=commentDtoList;
    }
}
