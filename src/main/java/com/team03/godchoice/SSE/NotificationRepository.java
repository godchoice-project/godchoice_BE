package com.team03.godchoice.SSE;

import com.team03.godchoice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select count(n) from Notification n where n.member.memberId =:memberId and n.readState = false")
    Long countUnReadStateNotifications(@Param("memberId") Long memberId);

    List<Notification> findAllByMemberOrderByIdDesc(Member member);
}
