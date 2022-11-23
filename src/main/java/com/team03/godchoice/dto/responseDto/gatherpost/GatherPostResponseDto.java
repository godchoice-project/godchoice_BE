package com.team03.godchoice.dto.responseDto.gatherpost;

import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.dto.responseDto.CommentDto;
import com.team03.godchoice.dto.responseDto.PostImgResDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GatherPostResponseDto {

    private Long userId;
    private String userImg;
    private String userName;
    private List<PostImgResDto> postImgInfo;
    private String category;
    private String date;
    private int number;
    private String kakaoLink;
    private String sex;
    private int startAge;
    private int endAge;
    private String title;
    private String content;
    private String postLink;
    private String postAddress;
    private String postState;
    private boolean bookMarkStatus;
    private long viewCount;
    private List<CommentDto> commentDtoList;
    public GatherPostResponseDto(GatherPost gatherPost, List<PostImgResDto> postImgResDtos,List<CommentDto> commentDtoList,boolean bookMarkStatus) {
        this.userId = gatherPost.getMember().getMemberId();
        this.userImg = gatherPost.getMember().getUserImgUrl();
        this.userName = gatherPost.getMember().getUserName();
        this.postImgInfo = postImgResDtos;
        this.category = gatherPost.getCategory().toString();
        this.date = gatherPost.getDate().toString();
        this.number = gatherPost.getNumber();
        this.kakaoLink = gatherPost.getKakaoLink();
        this.sex = gatherPost.getSex();
        this.startAge = gatherPost.getStartAge();
        this.endAge = gatherPost.getEndAge();
        this.title = gatherPost.getTitle();
        this.content = gatherPost.getPostLink();
        this.postLink = gatherPost.getPostLink();
        this.postAddress = gatherPost.getPostAddress();
        this.postState = gatherPost.getPostStatus();
        this.bookMarkStatus = bookMarkStatus;
        this.viewCount = gatherPost.getViewCount();
        this.commentDtoList=commentDtoList;
    }
}
