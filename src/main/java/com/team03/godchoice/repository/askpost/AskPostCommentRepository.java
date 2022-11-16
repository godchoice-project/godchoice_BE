package com.team03.godchoice.repository.askpost;

import com.team03.godchoice.domain.askpost.AskPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AskPostCommentRepository extends JpaRepository<AskPostComment, Long> {

    Optional<AskPostComment> findByCommentId(Long parentId);


}
