package com.team03.godchoice.repository;

import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GatherPostImgRepository extends JpaRepository<GatherPostImg, Long> {
    List<GatherPostImg> findAllByGatherPost(GatherPost gatherPost);

    void deleteAllByGatherPost(GatherPost gatherPost);

    GatherPostImg findByGatherPostImgId(Long gatherPostId);
}
