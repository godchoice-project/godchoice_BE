package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.domain.eventpost.EventPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostResDto {
    private String username;
    private List<String> postImgUrl;
    private String category;
    private String startPeriod;
    private String endPeriod;
    private String title;
    private String content;
    private String postLink;
    private String postAddress;
    private String postState;

    private List<CommentDto> commentDtoList;

    public EventPostResDto(EventPost eventPost, List<String> imgUrl, List<CommentDto> commentDtoList) {
        this.username = eventPost.getMember().getUserName();
        this.postImgUrl = imgUrl;
        this.category = eventPost.getCategory().toString();
        this.endPeriod = eventPost.getEndPeriod().toString();
        this.startPeriod = eventPost.getStartPeriod().toString();
        this.title = eventPost.getTitle();
        this.content = eventPost.getContent();
        this.postLink = eventPost.getPostLink();
        this.postAddress = eventPost.getPostAddress();
        this.postState = eventPost.getEventStatus();
        this.commentDtoList=commentDtoList;
    }
}
