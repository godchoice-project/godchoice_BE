package com.team03.godchoice.service;

import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.domainenum.DeleteStatus;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostComment;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.CommentRequestDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.askpost.AskPostCommentRepository;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import com.team03.godchoice.repository.eventpost.EventPostCommentRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostCommentRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AskPostCommentRepository askPostCommentRepository;
    private final EventPostCommentRepository eventPostCommentRepository;
    private final GatherPostCommentRepository gatherPostCommentRepository;
    private final AskPostRepository askPostRepository;
    private final EventPostRepository eventPostRepository;
    private final GatherPostRepository gatherPostRepository;

    public GlobalResDto<?> createComment(Long postId, String kind, CommentRequestDto commentRequestDto, Member account) {

        if (kind.equals("askPost")) {
            AskPost askPost = askPostRepository.findById(postId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_FOUND_POST)
            );
            AskPostComment parentComment = askPostCommentRepository
                    .findByCommentId(commentRequestDto.getParentId()).orElse(null);

            AskPostComment askPostComment = new AskPostComment(commentRequestDto, askPost, account, parentComment);

            askPostCommentRepository.save(askPostComment);
        }else if(kind.equals("eventPost")){
            EventPost eventPost = eventPostRepository.findById(postId).orElseThrow(
                    ()-> new CustomException(ErrorCode.NOT_FOUND_POST)
            );
            EventPostComment parentComment = eventPostCommentRepository
                    .findByCommentId(commentRequestDto.getParentId()).orElse(null);

            EventPostComment eventPostComment = new EventPostComment(commentRequestDto, eventPost, account, parentComment);

            eventPostCommentRepository.save(eventPostComment);
        } else{
            GatherPost gatherPost = gatherPostRepository.findById(postId).orElseThrow(
                    ()-> new CustomException(ErrorCode.NOT_FOUND_POST)
            );
            GatherPostComment parentComment = gatherPostCommentRepository
                    .findByCommentId(commentRequestDto.getParentId()).orElse(null);
            GatherPostComment gatherPostComment = new GatherPostComment(commentRequestDto, gatherPost, account, parentComment);

            gatherPostCommentRepository.save(gatherPostComment);
        }

        return GlobalResDto.success(null, "Success create comment");
    }

    public GlobalResDto<?> deleteComment(Long commentId, String  kind, Member member) {

        if(kind.equals("ask")){
            AskPostComment comment = askPostCommentRepository.findById(commentId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_FOUND_COMMENT)
            );
            if (!comment.getMember().getMemberId().equals(member.getMemberId())) {
                throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
            }
            if (!(comment.getParent() == null)) { // 자식 댓글임
                askPostCommentRepository.delete(comment);
            } else { // 부모 댓글임
                if (comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
                    comment.changeDeletedStatus(DeleteStatus.Y);
                } else { // 삭제 가능한 조상 댓글을 구해서 삭제
                    askPostCommentRepository.delete(comment);
                }
            }
        }else if(kind.equals("event")){
            EventPostComment comment = eventPostCommentRepository.findById(commentId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_FOUND_COMMENT)
            );
            if (!comment.getMember().getMemberId().equals(member.getMemberId())) {
                throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
            }
            if (!(comment.getParent() == null)) { // 자식 댓글임
                eventPostCommentRepository.delete(comment);
            } else { // 부모 댓글임
                if (comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
                    comment.changeDeletedStatus(DeleteStatus.Y);
                } else { // 삭제 가능한 조상 댓글을 구해서 삭제
                    eventPostCommentRepository.delete(comment);
                }
            }
        }else{
            GatherPostComment comment = gatherPostCommentRepository.findById(commentId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_FOUND_COMMENT)
            );
            if (!comment.getMember().getMemberId().equals(member.getMemberId())) {
                throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
            }
            if (!(comment.getParent() == null)) { // 자식 댓글임
                gatherPostCommentRepository.delete(comment);
            } else { // 부모 댓글임
                if (comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
                    comment.changeDeletedStatus(DeleteStatus.Y);
                } else { // 삭제 가능한 조상 댓글을 구해서 삭제
                    gatherPostCommentRepository.delete(comment);
                }
            }
        }

        return GlobalResDto.success(null, "Success delete comment");
    }
}
