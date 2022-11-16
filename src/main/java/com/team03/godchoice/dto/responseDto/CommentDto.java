package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.domain.eventpost.EventPostComment;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentDto {

    private Long commentId;
    private String userName;
    private String content;

    private List<AskPostComment> askPostCommentChildren = new ArrayList<>();
    private List<EventPostComment> eventPostCommentChildren = new ArrayList<>();
    private List<GatherPostComment> gatherPostCommentChildren = new ArrayList<>();

    public CommentDto(AskPostComment comment){
        this.commentId=comment.getCommentId();
        this.userName=comment.getMember().getUserName();
        this.content=comment.getContent();
        this.askPostCommentChildren=comment.getChildren();
    }

    public CommentDto(EventPostComment comment){
        this.commentId=comment.getCommentId();
        this.userName=comment.getMember().getUserName();
        this.content=comment.getContent();
        this.eventPostCommentChildren=comment.getChildren();
    }

    public CommentDto(GatherPostComment comment){
        this.commentId=comment.getCommentId();
        this.userName=comment.getMember().getUserName();
        this.content=comment.getContent();
        this.gatherPostCommentChildren=comment.getChildren();
    }
}
