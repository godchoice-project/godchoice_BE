package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.domain.Comment;
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

    private List<Comment> children = new ArrayList<>();

    public CommentDto(Comment comment){
        this.commentId=comment.getCommentId();
        this.userName=comment.getMember().getUserName();
        this.content=comment.getContent();
        this.children=comment.getChildren();
    }
}
