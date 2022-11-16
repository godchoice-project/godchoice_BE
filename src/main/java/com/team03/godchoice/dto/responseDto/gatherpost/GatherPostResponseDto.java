package com.team03.godchoice.dto.responseDto.gatherpost;

import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.dto.responseDto.CommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GatherPostResponseDto {
    private String userName;
    private List<String> postImgUrl;
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

    private List<CommentDto> commentDtoList;
    public GatherPostResponseDto(GatherPost gatherPost, List<String> imgUrl,List<CommentDto> commentDtoList) {
        this.userName = gatherPost.getMember().getUserName();
        this.postImgUrl = imgUrl;
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
        this.commentDtoList=commentDtoList;
    }
}
