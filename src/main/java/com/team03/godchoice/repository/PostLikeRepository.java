package com.team03.godchoice.repository;

import com.team03.godchoice.domain.AskPost;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByAskPostAndMember(AskPost askPost, Member member);


}
