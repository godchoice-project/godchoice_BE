package com.team03.godchoice.repository.gatherpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GatherPostRepository extends JpaRepository <GatherPost, Long> {
    List<GatherPost> findAllByMember(Member member);
}
