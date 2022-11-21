package com.team03.godchoice.config;

import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
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

    @Scheduled(cron = "0 0 0 * * *")
    public void checkEventStatus() {
        List<EventPost> eventPostList = eventPostRepository.findAll();
        for(EventPost eventPost : eventPostList){
            if(eventPost.getEventStatus().equals("진행중")){
                LocalDate now = LocalDate.now();
                if(eventPost.getEndPeriod().isBefore(now)){
                    eventPost.setEventStatus("마감");
                    eventPostRepository.save(eventPost);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkGatherStatus() {
        List<GatherPost> gatherPostList = gatherPostRepository.findAll();
        for(GatherPost gatherPost : gatherPostList){
            if(gatherPost.getPostStatus().equals("진행중")){
                LocalDate now = LocalDate.now();
                if(gatherPost.getDate().isBefore(now)){
                    gatherPost.setPostStatus("마감");
                    gatherPostRepository.save(gatherPost);
                }
            }
        }
    }

    /*
    스케쥴러 config 클래스입니다.
    매일 정각에 행사글 상태가 진행중인 포스트를 뽑아 그중에서 endPeriod가 오늘보다 전이라면 상태를 종료로 바꾸는 스케쥴러입니다.
    */
    /*
    비동기 설정으로 SSE 구현하기
     */

}
