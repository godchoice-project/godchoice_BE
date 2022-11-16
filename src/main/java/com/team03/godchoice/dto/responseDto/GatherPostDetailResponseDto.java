package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostImg;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostImg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GatherPostDetailResponseDto {

    private Long postId;
    private String title;
    private String content;
    private List<String > gatherPostImgUrl;
    private List<CommentDto> commentDtoList;

    public GatherPostDetailResponseDto(GatherPost gatherPost, List<String> gatherPostImgUrl, List<CommentDto> commentDtoList){

        this.postId=gatherPost.getGatherPostId();
        this.title=gatherPost.getTitle();
        this.content=gatherPost.getContent();
        this.gatherPostImgUrl=gatherPostImgUrl;
        this.commentDtoList=commentDtoList;
    }
}
