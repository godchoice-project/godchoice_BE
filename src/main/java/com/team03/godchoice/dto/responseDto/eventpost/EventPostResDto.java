package com.team03.godchoice.dto.responseDto.eventpost;

import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.dto.responseDto.CommentDto;
import com.team03.godchoice.dto.responseDto.PostImgResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostResDto {
    private Long userId;
    private String userImg;
    private String username;
    private List<PostImgResDto> postImgInfo;
    private String category;
    private String startPeriod;
    private String endPeriod;
    private String title;
    private String content;
    private String postLink;
    private String postAddress;
    private String postState;
    private boolean bookMarkStatus;

    private List<CommentDto> commentDtoList;

    public EventPostResDto(EventPost eventPost, List<PostImgResDto> postImgResDtos, List<CommentDto> commentDtoList,boolean bookMarkStatus) {
        this.userId = eventPost.getMember().getMemberId();
        this.userImg = eventPost.getMember().getUserImgUrl();
        this.username = eventPost.getMember().getUserName();
        this.postImgInfo = postImgResDtos;
        this.category = eventPost.getCategory().toString();
        this.endPeriod = eventPost.getEndPeriod().toString();
        this.startPeriod = eventPost.getStartPeriod().toString();
        this.title = eventPost.getTitle();
        this.content = eventPost.getContent();
        this.postLink = eventPost.getPostLink();
        this.postAddress = eventPost.getPostAddress();
        this.postState = eventPost.getEventStatus();
        this.bookMarkStatus = bookMarkStatus;
        this.commentDtoList=commentDtoList;
    }
}
