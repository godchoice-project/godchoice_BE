package com.team03.godchoice.repository.askpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPostLike;
import com.team03.godchoice.domain.askpost.AskPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AskPostLikeRepository extends JpaRepository<AskPostLike, Long> {

    Optional<AskPostLike> findByAskPostAndMember(AskPost askPost, Member member);
}
