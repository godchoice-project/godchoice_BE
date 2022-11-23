package com.team03.godchoice.dto.responseDto.mypageResDto;


import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPagePostResDto {
    private Long postId;
    private String postImg;
    private String postTitle;

    public MyPagePostResDto(EventPost eventPost) {
        this.postId = eventPost.getEventPostId();
        this.postImg = eventPost.getPostImgUrl().get(0).getImgUrl();
        this.postTitle = eventPost.getTitle();
    }

    public MyPagePostResDto(GatherPost gatherPost) {
        this.postId = gatherPost.getGatherPostId();
        this.postImg = gatherPost.getGatherPostImg().get(0).getImgUrl();
        this.postTitle = gatherPost.getTitle();
    }

    public MyPagePostResDto(AskPost askPost) {
        this.postId = askPost.getAskPostId();
        this.postImg = askPost.getAskPostImg().get(0).getImage();
        this.postTitle = askPost.getTitle();
    }
}