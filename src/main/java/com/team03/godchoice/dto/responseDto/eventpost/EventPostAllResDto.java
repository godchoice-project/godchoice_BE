package com.team03.godchoice.dto.responseDto.eventpost;

import com.team03.godchoice.domain.eventpost.EventPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostAllResDto {
    private Long postId;
    private String title;
    private String category;
    private String startPeriod;
    private String endPeriod;
    private String eventStatus;
    private String imgUrl;
    private String content;
    private long viewCount;
    private boolean bookMarkStatus;

    public static EventPostAllResDto toEPARD(EventPost eventPost,boolean bookMarkStatus) {
        return new EventPostAllResDto(
                eventPost.getEventPostId(),
                eventPost.getTitle(),
                eventPost.getCategory().toString(),
                eventPost.getStartPeriod().toString(),
                eventPost.getEndPeriod().toString(),
                eventPost.getEventStatus(),
                toImgUrl(eventPost),
                toContent(eventPost.getContent()),
                eventPost.getViewCount(),
                bookMarkStatus
        );
    }

    public static String toImgUrl(EventPost eventPost){
        if(eventPost.getPostImgUrl()==null || eventPost.getPostImgUrl().isEmpty()){
            return "https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_post_img.png";
        }else{
            return eventPost.getPostImgUrl().get(0).getImgUrl();
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
