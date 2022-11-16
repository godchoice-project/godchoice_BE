package com.team03.godchoice.dto.responseDto.gatherpost;

import com.team03.godchoice.domain.gatherPost.GatherPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GatherPostAllResDto {
    private String title;
    private String category;
    private int number;
    private int startAge;
    private int endAge;
    private String date;
    private String sex;
    private String imgUrl;

    public static GatherPostAllResDto toGPARD(GatherPost gatherPost) {
        return new GatherPostAllResDto(
                gatherPost.getTitle(),
                gatherPost.getCategory().toString(),
                gatherPost.getNumber(),
                gatherPost.getStartAge(),
                gatherPost.getEndAge(),
                gatherPost.getDate().toString(),
                gatherPost.getSex(),
                gatherPost.getGatherPostImg().get(0).getImgUrl()
        );
    }
}
