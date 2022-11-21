package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.abstrctPackage.TimeCountClass;
import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.domain.domainenum.DeleteStatus;
import com.team03.godchoice.domain.eventpost.EventPostComment;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentDto extends TimeCountClass {

    private String commentWriteDate;
    private Long commentId;
    private String userName;
    private String userImg;
    private String content;

    private List<CommentChildDto> askPostCommentChildren = new ArrayList<>();
    private List<CommentChildDto> eventPostCommentChildren = new ArrayList<>();
    private List<CommentChildDto> gatherPostCommentChildren = new ArrayList<>();

    public CommentDto(AskPostComment comment){
        this.commentWriteDate = countTime(comment.getCreatedAt());
        this.commentId=comment.getCommentId();
        this.askPostCommentChildren=CommentChildDto.toAskPostCommentChildDto(comment.getChildren());
        if(comment.getIsDeleted().equals(DeleteStatus.N)){
            this.userName=comment.getMember().getUserName();
            this.userImg=comment.getMember().getUserImgUrl();
            this.content=comment.getContent();
        }else{
            this.userName="알수없음";
            this.userImg="https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_profile.jpg";
            this.content="알수없음";
        }
    }

    public CommentDto(EventPostComment comment){
        this.commentWriteDate = countTime(comment.getCreatedAt());
        this.commentId=comment.getCommentId();
        this.eventPostCommentChildren=CommentChildDto.toEventPostCommentChildDto(comment.getChildren());
        if(comment.getIsDeleted().equals(DeleteStatus.N)){
            this.userName=comment.getMember().getUserName();
            this.userImg=comment.getMember().getUserImgUrl();
            this.content=comment.getContent();
        }else{
            this.userName="알수없음";
            this.userImg="https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_profile.jpg";
            this.content="알수없음";
        }

    }

    public CommentDto(GatherPostComment comment){
        this.commentWriteDate = countTime(comment.getCreatedAt());
        this.commentId=comment.getCommentId();
        this.gatherPostCommentChildren=CommentChildDto.toGatherPostCommentChildDto(comment.getChildren());
        if(comment.getIsDeleted().equals(DeleteStatus.N)){
            this.userName=comment.getMember().getUserName();
            this.userImg=comment.getMember().getUserImgUrl();
            this.content=comment.getContent();
        }else{
            this.userName="알수없음";
            this.userImg="https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_profile.jpg";
            this.content="알수없음";
        }
    }
}
