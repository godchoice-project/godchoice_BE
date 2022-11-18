package com.team03.godchoice.repository.eventpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventPostLikeRepository extends JpaRepository<EventPostLike, Long> {

    Optional<EventPostLike> findByEventPostAndMember(EventPost eventPost, Member member);

    List<EventPostLike> findAllByMember(Member member);

    List<EventPostLike> findAllByMemberOrderByPostLikeIdDesc(Member member);

    boolean existsByMemberAndEventPost(Member member, EventPost eventPost);
}
