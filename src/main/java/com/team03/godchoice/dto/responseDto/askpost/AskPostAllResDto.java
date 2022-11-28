package com.team03.godchoice.dto.responseDto.askpost;

import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.eventpost.EventPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AskPostAllResDto {
    private Long postId;
    private long viewCount;
    private String ImgUrl;
    private String title;
    private String content;
    private boolean bookMarkStatus;

    public static AskPostAllResDto toAPARD(AskPost askPost, boolean bookMarkStatus) {
        return new AskPostAllResDto(
                askPost.getAskPostId(),
                askPost.getViewCount(),
                toImgUrl(askPost),
                askPost.getTitle(),
                toContent(askPost.getContent()),
                bookMarkStatus
        );
    }

    public static String toImgUrl(AskPost askPost) {
        if (askPost.getAskPostImg() == null || askPost.getAskPostImg().isEmpty()) {
            return "https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_post_img.png";
        } else {
            return askPost.getAskPostImg().get(0).getImage();
        }
    }

    public static String toContent(String content){
        if(content.length()>=10){
            return content.substring(0,10).trim()+"...";
        }else {
            return content.trim();
        }
    }
}
