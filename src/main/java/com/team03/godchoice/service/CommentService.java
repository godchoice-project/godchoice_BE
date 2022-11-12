package com.team03.godchoice.service;

import com.team03.godchoice.domain.Comment;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.domainenum.DeleteStatus;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.CommentRequestDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.CommentRepository;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AskPostRepository askPostRepository;

    public GlobalResDto createComment(Long postId, CommentRequestDto commentRequestDto, Member account) {
        AskPost askPost = askPostRepository.findById(postId).orElseThrow(
                ()-> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        Comment parentComment = commentRepository
                .findByCommentId(commentRequestDto.getParentId()).orElse(null);


        Comment comment = new Comment(commentRequestDto, askPost, account, parentComment);


        commentRepository.save(comment);

        return GlobalResDto.success(null,"Success create comment");
    }

    public GlobalResDto deleteComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_COMMENT)
        );

        if(!comment.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
        }

        if(!(comment.getParent()==null)){
            commentRepository.delete(comment);
        }else{
            if(comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
                comment.changeDeletedStatus(DeleteStatus.Y);
            } else { // 삭제 가능한 조상 댓글을 구해서 삭제
                commentRepository.delete(comment);
            }
        }

        return GlobalResDto.success(null,"Success delete comment");
    }
}
