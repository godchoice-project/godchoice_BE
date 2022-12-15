package com.team03.godchoice.repository.eventpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.eventpost.EventPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventPostRepository extends JpaRepository<EventPost,Long>{
    Optional<EventPost> findByEventPostId(Long postId);

    List<EventPost> findAllByMemberOrderByEventPostIdDesc(Member member);
}
