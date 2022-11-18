package com.team03.godchoice.repository.gatherpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GatherPostCommentRepository extends JpaRepository<GatherPostComment, Long> {

    Optional<GatherPostComment> findByCommentId(Long parentId);

    List<GatherPostComment> findAllByMember(Member member);

    List<GatherPostComment> findAllByMemberOrderByCommentIdDesc(Member member);
}
