package com.team03.godchoice.repository.eventpost;

import antlr.collections.impl.ASTArray;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.eventpost.EventPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventPostCommentRepository extends JpaRepository<EventPostComment, Long> {

    Optional<EventPostComment> findByCommentId(Long parentId);

    List<EventPostComment> findAllByMember(Member member);

    List<EventPostComment> findAllByMemberOrderByCommentIdDesc(Member member);

    List<EventPostComment> findAllByParent(EventPostComment parent);
}
