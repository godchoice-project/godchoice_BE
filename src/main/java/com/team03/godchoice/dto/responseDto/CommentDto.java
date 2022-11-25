package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.abstrctPackage.TimeCountClass;
import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.enumclass.DeleteStatus;
import com.team03.godchoice.domain.eventpost.EventPostComment;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class CommentDto extends TimeCountClass {

    private String commentWriteDate;
    private Long commentId;
    private Long userId;
    private String userName;
    private String userImg;
    private String content;

    private List<CommentChildDto> askPostCommentChildren = new ArrayList<>();
    private List<CommentChildDto> eventPostCommentChildren = new ArrayList<>();
    private List<CommentChildDto> gatherPostCommentChildren = new ArrayList<>();

    public CommentDto(AskPostComment comment) {
        this.commentWriteDate = countTime(comment.getCreatedAt());
        this.commentId = comment.getCommentId();
        this.askPostCommentChildren = reverseList(CommentChildDto.toAskPostCommentChildDto(comment.getChildren()));
        if (comment.getIsDeleted().equals(DeleteStatus.N)) {
            this.userId = comment.getMember().getMemberId();
            this.userName = comment.getMember().getUserName();
            this.userImg = comment.getMember().getUserImgUrl();
            this.content = comment.getContent();
        } else {
            this.userId = 0L;
            this.userName = "알수없음";
            this.userImg = "https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_user_img.png";
            this.content = "알수없음";
        }
    }

    public CommentDto(EventPostComment comment) {
        this.commentWriteDate = countTime(comment.getCreatedAt());
        this.commentId = comment.getCommentId();
        this.eventPostCommentChildren = reverseList(CommentChildDto.toEventPostCommentChildDto(comment.getChildren()));
        if (comment.getIsDeleted().equals(DeleteStatus.N)) {
            this.userId = comment.getMember().getMemberId();
            this.userName = comment.getMember().getUserName();
            this.userImg = comment.getMember().getUserImgUrl();
            this.content = comment.getContent();
        } else {
            this.userId = 0L;
            this.userName = "알수없음";
            this.userImg = "https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_user_img.png";
            this.content = "알수없음";
        }

    }

    public CommentDto(GatherPostComment comment) {
        this.commentWriteDate = countTime(comment.getCreatedAt());
        this.commentId = comment.getCommentId();
        this.gatherPostCommentChildren = reverseList(CommentChildDto.toGatherPostCommentChildDto(comment.getChildren()));
        if (comment.getIsDeleted().equals(DeleteStatus.N)) {
            this.userId = comment.getMember().getMemberId();
            this.userName = comment.getMember().getUserName();
            this.userImg = comment.getMember().getUserImgUrl();
            this.content = comment.getContent();
        } else {
            this.userId = 0L;
            this.userName = "알수없음";
            this.userImg = "https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_user_img.png";
            this.content = "알수없음";
        }
    }

    private List<CommentChildDto> reverseList(List<CommentChildDto> commentChildDto){
        Collections.reverse(commentChildDto);
        return commentChildDto;
    }
}
