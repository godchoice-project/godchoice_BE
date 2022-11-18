package com.team03.godchoice.repository.askpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPostLike;
import com.team03.godchoice.domain.askpost.AskPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AskPostLikeRepository extends JpaRepository<AskPostLike, Long> {

    Optional<AskPostLike> findByAskPostAndMember(AskPost askPost, Member member);

    List<AskPostLike> findAllByMember(Member member);

    List<AskPostLike> findAllByMemberOrderByPostLikeIdDesc(Member member);

    boolean existsByMemberAndAskPost(Member member, AskPost askPost);
}
