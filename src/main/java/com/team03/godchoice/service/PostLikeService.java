package com.team03.godchoice.service;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPostLike;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostLike;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostLike;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.askpost.AskPostLikeRepository;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import com.team03.godchoice.repository.eventpost.EventPostLikeRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostLikeRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostRepository;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final AskPostRepository askPostRepository;
    private final EventPostRepository eventPostRepository;
    private final GatherPostRepository gatherPostRepository;
    private final AskPostLikeRepository askPostLikeRepository;
    private final EventPostLikeRepository eventPostLikeRepository;
    private final GatherPostLikeRepository gatherPostLikeRepository;

    @Transactional
    public GlobalResDto<?> createPostLike(Long postId, String kind, UserDetailsImpl userDetails) {
        // 유저 정보 가져오기
        Member member = userDetails.getMember();

        if(kind.equals("ask")){
            AskPost post = askPostRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

            // 좋아요를 누른 상태인지, 누르지 않은 상태인지 확인
            Optional<AskPostLike> foundLike = askPostLikeRepository.findByAskPostAndMember(post, member);

            if (foundLike.isPresent()) {
                askPostLikeRepository.delete(foundLike.get());

                return GlobalResDto.success(null,"게시글 찜 취소 완료");

            } else {
                AskPostLike postLike = new AskPostLike(member, post);
                askPostLikeRepository.save(postLike);

                return GlobalResDto.success(null,"게시글 찜 완료");
            }
        }else if(kind.equals("event")){
            EventPost post = eventPostRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

            // 좋아요를 누른 상태인지, 누르지 않은 상태인지 확인
            Optional<EventPostLike> foundLike = eventPostLikeRepository.findByEventPostAndMember(post, member);

            if (foundLike.isPresent()) {
                eventPostLikeRepository.delete(foundLike.get());

                return GlobalResDto.success(null,"게시글 찜 취소 완료");

            } else {
                EventPostLike postLike = new EventPostLike(member, post);
                eventPostLikeRepository.save(postLike);

                return GlobalResDto.success(null,"게시글 찜 완료");
            }
        }else{
            GatherPost post  = gatherPostRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

            // 좋아요를 누른 상태인지, 누르지 않은 상태인지 확인
            Optional<GatherPostLike> foundLike = gatherPostLikeRepository.findByGatherPostAndMember(post, member);

            if (foundLike.isPresent()) {
                gatherPostLikeRepository.delete(foundLike.get());

                return GlobalResDto.success(null,"게시글 찜 취소 완료");

            } else {
                GatherPostLike postLike = new GatherPostLike(member, post);
                gatherPostLikeRepository.save(postLike);

                return GlobalResDto.success(null,"게시글 찜 완료");
            }
        }
    }
}
