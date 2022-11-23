package com.team03.godchoice.repository.askpost;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.domain.askpost.AskPostImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AskPostCommentRepository extends JpaRepository<AskPostComment, Long> {

    Optional<AskPostComment> findByCommentId(Long parentId);


    List<AskPostComment> findAllByMember(Member member);

    List<AskPostComment> findAllByMemberOrderByCommentIdDesc(Member member);

    List<AskPostComment> findAllByParent(AskPostComment askPostComment);
}
