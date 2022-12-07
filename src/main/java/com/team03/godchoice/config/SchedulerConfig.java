package com.team03.godchoice.config;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerConfig {

    private final EventPostRepository eventPostRepository;
    private final GatherPostRepository gatherPostRepository;
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkEventStatus() {
        List<EventPost> eventPostList = eventPostRepository.findAll();
        for (EventPost eventPost : eventPostList) {
            if (eventPost.getEventStatus().equals("진행중")) {
                LocalDate now = LocalDate.now();
                if (eventPost.getEndPeriod().isBefore(now)) {
                    eventPost.setEventStatus("마감");
                    eventPostRepository.save(eventPost);
                }
            }
        }

        List<GatherPost> gatherPostList = gatherPostRepository.findAll();
        for (GatherPost gatherPost : gatherPostList) {
            if (gatherPost.getPostStatus().equals("진행중")) {
                LocalDate now = LocalDate.now();
                if (gatherPost.getDate().isBefore(now)) {
                    gatherPost.setPostStatus("마감");
                    gatherPostRepository.save(gatherPost);
                }
            }
        }

        List<Member> memberList = memberRepository.findAll();
        for (Member member : memberList) {
            if (member.getPostView() != null) {
                member.setPostView(null);
                memberRepository.save(member);
            }
        }
    }

}
