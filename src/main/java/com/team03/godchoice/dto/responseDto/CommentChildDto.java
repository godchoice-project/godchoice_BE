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
public class CommentChildDto {

    private Long commentId;
    private Long parentId;
    private String userName;
    private String content;

    public CommentChildDto(EventPostComment eventPostComment){
        this.commentId = eventPostComment.getCommentId();
        this.parentId = eventPostComment.getParent().getCommentId();
        this.userName = eventPostComment.getMember().getUserName();
        this.content = eventPostComment.getContent();
    }

    public CommentChildDto(AskPostComment askPostComment){
        this.commentId = askPostComment.getCommentId();
        this.parentId = askPostComment.getParent().getCommentId();
        this.userName = askPostComment.getMember().getUserName();
        this.content = askPostComment.getContent();
    }

    public CommentChildDto(GatherPostComment gatherPostComment){
        this.commentId = gatherPostComment.getCommentId();
        this.parentId = gatherPostComment.getParent().getCommentId();
        this.userName = gatherPostComment.getMember().getUserName();
        this.content = gatherPostComment.getContent();
    }

    public static List<CommentChildDto> toEventPostCommentChildDto(List<EventPostComment> eventPostCommentList){
        List<CommentChildDto> commentChildDtos = new ArrayList<>();
        for(EventPostComment eventPostComment : eventPostCommentList){
            CommentChildDto commentChildDto = new CommentChildDto(eventPostComment);
            commentChildDtos.add(commentChildDto);
        }
        return commentChildDtos;
    }

    public static List<CommentChildDto> toAskPostCommentChildDto(List<AskPostComment> askPostCommentList){
        List<CommentChildDto> commentChildDtos = new ArrayList<>();
        for(AskPostComment askPostComment : askPostCommentList){
            CommentChildDto commentChildDto = new CommentChildDto(askPostComment);
            commentChildDtos.add(commentChildDto);
        }
        return commentChildDtos;
    }

    public static List<CommentChildDto> toGatherPostCommentChildDto(List<GatherPostComment> gatherPostCommentList){
        List<CommentChildDto> commentChildDtos = new ArrayList<>();
        for(GatherPostComment gatherPostComment : gatherPostCommentList){
            CommentChildDto commentChildDto = new CommentChildDto(gatherPostComment);
            commentChildDtos.add(commentChildDto);
        }
        return commentChildDtos;
    }

}
