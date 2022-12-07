package com.team03.godchoice.repository.gatherpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GatherPostLikeRepository extends JpaRepository<GatherPostLike, Long> {

    Optional<GatherPostLike> findByGatherPostAndMember(GatherPost gatherPost, Member member);

    List<GatherPostLike> findAllByMemberOrderByPostLikeIdDesc(Member member);

    boolean existsByMemberAndGatherPost(Member member, GatherPost gatherPost);
}
