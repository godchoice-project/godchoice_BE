package com.team03.godchoice.dto.responseDto.eventpost;

import com.team03.godchoice.domain.eventpost.EventPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostAllResDto {
    private String title;
    private String category;
    private String startPeriod;
    private String endPeriod;
    private String eventStatus;
    private String imgUrl;

    public static EventPostAllResDto toEPARD(EventPost eventPost) {
        return new EventPostAllResDto(
                eventPost.getTitle(),
                eventPost.getCategory().toString(),
                eventPost.getStartPeriod().toString(),
                eventPost.getEndPeriod().toString(),
                eventPost.getEventStatus(),
                eventPost.getPostImgUrl().get(0).getImgUrl()
        );
    }
}
