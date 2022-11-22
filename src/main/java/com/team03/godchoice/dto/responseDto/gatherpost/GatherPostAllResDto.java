package com.team03.godchoice.dto.responseDto.gatherpost;

import com.team03.godchoice.domain.gatherPost.GatherPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GatherPostAllResDto {
    private Long postId;
    private String title;
    private String category;
    private int number;
    private int startAge;
    private int endAge;
    private String date;
    private String sex;
    private String imgUrl;
    private String content;
    private long viewCount;
    private boolean bookMarkStatus;

    public static GatherPostAllResDto toGPARD(GatherPost gatherPost,boolean bookMarkStatus) {
        return new GatherPostAllResDto(
                gatherPost.getGatherPostId(),
                gatherPost.getTitle(),
                gatherPost.getCategory().toString(),
                gatherPost.getNumber(),
                gatherPost.getStartAge(),
                gatherPost.getEndAge(),
                gatherPost.getDate().toString(),
                gatherPost.getSex(),
                toImgUrl(gatherPost),
                toContent(gatherPost.getContent()),
                gatherPost.getViewCount(),
                bookMarkStatus
        );
    }

    public static String toImgUrl(GatherPost gatherPost){
        if(gatherPost.getGatherPostImg()==null || gatherPost.getGatherPostImg().isEmpty()){
            return "https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_profile.jpg";
        }else{
            return gatherPost.getGatherPostImg().get(0).getImgUrl();
        }
    }

    public static String toContent(String content){
        if(content.length()>=10){
            return content.substring(10).trim()+"...";
        }else {
            return content.trim();
        }
    }
}
