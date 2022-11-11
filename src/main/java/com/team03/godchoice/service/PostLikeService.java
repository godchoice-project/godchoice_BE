package com.team03.godchoice.service;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.PostLike;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.PostLikeRepository;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final AskPostRepository askPostRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public GlobalResDto<?> createPostLike(Long postId, String kind, UserDetailsImpl userDetails) {

        AskPost post = askPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));


        // 유저 정보 가져오기
        Member member = userDetails.getMember();

        // 좋아요를 누른 상태인지, 누르지 않은 상태인지 확인
        Optional<PostLike> foundLike = postLikeRepository.findByAskPostAndMember(post, member);

        if (foundLike.isPresent()) {
            postLikeRepository.delete(foundLike.get());

            return GlobalResDto.success(null,"게시글 찜 취소 완료");

        } else {
            PostLike postLike = new PostLike(member, post);
            postLikeRepository.save(postLike);

            return GlobalResDto.success(null,"게시글 찜 완료");
        }
    }
}
