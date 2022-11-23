package com.team03.godchoice.dto.responseDto.askpost;

import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostImg;
import com.team03.godchoice.dto.responseDto.CommentDto;
import com.team03.godchoice.dto.responseDto.PostImgResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AskPostResponseDto {

    private Long userId;
    private String userImg;
    private String userName;
    private Long postId;
    private String title;
    private String content;
    private String postAddress;
    private String postLink;
    private boolean bookMarkStatus;
    private long viewCount;
    private List<PostImgResDto> askPostImgList;
    private List<CommentDto> commentDtoList;

    public AskPostResponseDto(AskPost askPost, List<PostImgResDto> askPostImgList, List<CommentDto> commentDtoList,boolean bookMarkStatus) {
        this.userId = askPost.getMember().getMemberId();
        this.userImg = askPost.getMember().getUserImgUrl();
        this.userName = askPost.getMember().getUserName();
        this.postId = askPost.getAskPostId();
        this.title = askPost.getTitle();
        this.content = askPost.getContent();
        this.postAddress = askPost.getPostAddress();
        this.postLink = askPost.getPostLink();
        this.bookMarkStatus = bookMarkStatus;
        this.viewCount = askPost.getViewCount();
        this.askPostImgList = askPostImgList;
        this.commentDtoList = commentDtoList;
    }
}
