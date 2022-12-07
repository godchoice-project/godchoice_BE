package com.team03.godchoice.repository.gatherpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GatherPostRepository extends JpaRepository <GatherPost, Long> {

    Optional<GatherPost> findByGatherPostId(Long gatherPostId);

    List<GatherPost> findAllByMemberOrderByGatherPostIdDesc(Member member);
}
