package com.team03.godchoice.repository.eventpost;

import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventPostImgRepository extends JpaRepository<EventPostImg,Long> {
    EventPostImg findByEventPostImgId(Long eventPostId);

    void deleteAllByEventPost(EventPost eventPost);

    List<EventPostImg> findAllByEventPost(EventPost eventPost);

    void deleteByEventPostImgId(Long eventPostId);
}
