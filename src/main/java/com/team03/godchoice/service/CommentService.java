package com.team03.godchoice.service;

import com.team03.godchoice.SSE.AlarmType;
import com.team03.godchoice.SSE.NotificationService;
import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.enumclass.DeleteStatus;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostComment;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.CommentRequestDto;
import com.team03.godchoice.dto.responseDto.CommentDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final NotificationService notificationService;
    private final AskPostCommentRepository askPostCommentRepository;
    private final EventPostCommentRepository eventPostCommentRepository;
    private final GatherPostCommentRepository gatherPostCommentRepository;
    private final AskPostRepository askPostRepository;
    private final EventPostRepository eventPostRepository;
    private final GatherPostRepository gatherPostRepository;

    public GlobalResDto<?> createComment(Long postId, String kind, CommentRequestDto commentRequestDto, Member account) {

        if (kind.equals("ask")) {
            AskPost askPost = askPostRepository.findById(postId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_FOUND_POST)
            );
            AskPostComment parentComment = askPostCommentRepository
                    .findByCommentId(commentRequestDto.getParentId()).orElse(null);

            AskPostComment askPostComment = new AskPostComment(commentRequestDto, askPost, account, parentComment);

            if(parentComment!=null){
                if(!parentComment.getAskPost().getAskPostId().equals(askPostComment.getAskPost().getAskPostId())){
                    throw  new CustomException(ErrorCode.COMMENT_ERROR);
                }
                notificationService.send(parentComment.getMember(),AlarmType.askPostCommentComment,commentRequestDto.getContent(),askPost.getAskPostId(),"댓글에 대댓글이 달렸습니다.");

            }else{
                if(!askPost.getMember().equals(askPostComment.getMember())){
                    notificationService.send(askPost.getMember(), AlarmType.askPostComment,commentRequestDto.getContent(),askPost.getAskPostId(),"질문글에 댓글이 달렸습니다.");
                }
            }

            askPostCommentRepository.save(askPostComment);
        }else if(kind.equals("event")){
            EventPost eventPost = eventPostRepository.findById(postId).orElseThrow(
                    ()-> new CustomException(ErrorCode.NOT_FOUND_POST)
            );
            EventPostComment parentComment = eventPostCommentRepository
                    .findByCommentId(commentRequestDto.getParentId()).orElse(null);

            EventPostComment eventPostComment = new EventPostComment(commentRequestDto, eventPost, account, parentComment);

            if(parentComment!=null){
                if(!parentComment.getEventPost().getEventPostId().equals(eventPostComment.getEventPost().getEventPostId())){
                    throw  new CustomException(ErrorCode.COMMENT_ERROR);
                }
                notificationService.send(parentComment.getMember(),AlarmType.eventPostCommentComment,commentRequestDto.getContent(),eventPost.getEventPostId(),"댓글에 대댓글이 달렸습니다.");

            }else{
                if(!eventPost.getMember().equals(eventPostComment.getMember())){
                    notificationService.send(eventPost.getMember(), AlarmType.eventPostComment,commentRequestDto.getContent(),eventPost.getEventPostId(),"행사글에 댓글이 달렸습니다.");
                }
            }

            eventPostCommentRepository.save(eventPostComment);
        } else{
            GatherPost gatherPost = gatherPostRepository.findById(postId).orElseThrow(
                    ()-> new CustomException(ErrorCode.NOT_FOUND_POST)
            );
            GatherPostComment parentComment = gatherPostCommentRepository
                    .findByCommentId(commentRequestDto.getParentId()).orElse(null);
            GatherPostComment gatherPostComment = new GatherPostComment(commentRequestDto, gatherPost, account, parentComment);

            if(parentComment!=null){
                if(!parentComment.getGatherPost().getGatherPostId().equals(gatherPostComment.getGatherPost().getGatherPostId())){
                    throw  new CustomException(ErrorCode.COMMENT_ERROR);
                }
                notificationService.send(parentComment.getMember(),AlarmType.gatherPostCommentComment,commentRequestDto.getContent(),gatherPost.getGatherPostId(),"댓글에 대댓글이 달렸습니다.");

            }else{
                if(!gatherPost.getMember().equals(gatherPostComment.getMember())){
                    notificationService.send(gatherPost.getMember(), AlarmType.gatherPostComment,commentRequestDto.getContent(),gatherPost.getGatherPostId(),"모집글에 댓글이 달렸습니다.");
                }
            }

            gatherPostCommentRepository.save(gatherPostComment);
        }

        return GlobalResDto.success(getComment(postId,kind).getData(), "Success create comment");
    }

    public GlobalResDto<?> deleteComment(Long postId ,Long commentId, String  kind, Member member) {

        if(kind.equals("ask")){
            AskPostComment comment = askPostCommentRepository.findById(commentId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_FOUND_COMMENT)
            );
            if (!comment.getMember().getMemberId().equals(member.getMemberId())) {
                throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
            }
            if (!(comment.getParent() == null)) { // 자식 댓글임
                if(askPostCommentRepository.findAllByParent(comment.getParent()).size() == 1){
                    if(comment.getParent().getIsDeleted().equals(DeleteStatus.Y)){
                        askPostCommentRepository.delete(comment.getParent());
                    }
                }
                askPostCommentRepository.delete(comment);
            } else { // 부모 댓글임
                if (comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
                    comment.changeDeletedStatus(DeleteStatus.Y);
                    askPostCommentRepository.save(comment);
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
                if(eventPostCommentRepository.findAllByParent(comment.getParent()).size()==1){
                    if(comment.getParent().getIsDeleted().equals(DeleteStatus.Y)){
                        eventPostCommentRepository.delete(comment.getParent());
                    }
                }
                eventPostCommentRepository.delete(comment);
            } else { // 부모 댓글임
                if (comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
                    comment.changeDeletedStatus(DeleteStatus.Y);
                    eventPostCommentRepository.save(comment);
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
                if(gatherPostCommentRepository.findAllByParent(comment.getParent()).size()==1){
                    if(comment.getParent().getIsDeleted().equals(DeleteStatus.Y)){
                        gatherPostCommentRepository.delete(comment.getParent());
                    }
                }
                gatherPostCommentRepository.delete(comment);
                //마지막 대댓글인경우 알수없음 댓글 삭제

            } else { // 부모 댓글임
                if (comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
                    comment.changeDeletedStatus(DeleteStatus.Y);
                    gatherPostCommentRepository.save(comment);
                } else { // 삭제 가능한 조상 댓글을 구해서 삭제
                    gatherPostCommentRepository.delete(comment);
                }
            }
        }

        return GlobalResDto.success(getComment(postId,kind).getData(), "Success delete comment");
    }

    public GlobalResDto<?> getComment(Long postId, String kind) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        if(kind.equals("ask")){
            AskPost askPost = askPostRepository.findByAskPostId(postId).orElseThrow(()->  new CustomException(ErrorCode.NOT_FOUND_POST));
            for(AskPostComment comment : askPost.getComments()){
                if(comment.getParent()==null){
                    commentDtoList.add(0,new CommentDto(comment));
                }
            }
        } else if (kind.equals("event")) {
            EventPost eventPost = eventPostRepository.findByEventPostId(postId).orElseThrow(()->  new CustomException(ErrorCode.NOT_FOUND_POST));
            for(EventPostComment comment : eventPost.getComments()){
                if(comment.getParent()==null){
                    commentDtoList.add(0,new CommentDto(comment));
                }
            }
        }else {
            GatherPost gatherPost = gatherPostRepository.findByGatherPostId(postId).orElseThrow(()->  new CustomException(ErrorCode.NOT_FOUND_POST));
            for(GatherPostComment comment : gatherPost.getComments()){
                if(comment.getParent() == null){
                    commentDtoList.add(0,new CommentDto(comment));
                }
            }
        }

        return GlobalResDto.success(commentDtoList,null);
    }

}
