package com.team03.godchoice.repository.askpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AskPostRepository extends JpaRepository<AskPost, Long> {

    List<AskPost> findAllByOrderByCreatedAtDesc();

    List<AskPost> findAllByMember(Member member);

    Optional<AskPost> findByAskPostId(Long askPostId);
}
