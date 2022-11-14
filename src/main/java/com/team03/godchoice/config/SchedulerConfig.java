package com.team03.godchoice.config;

import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerConfig {

    private final EventPostRepository eventPostRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkEventStatus() {
        List<EventPost> eventPostList = eventPostRepository.findAll();
        for(EventPost eventPost : eventPostList){
            if(eventPost.getEventStatus().equals("진행중")){
                LocalDate now = LocalDate.now();
                if(eventPost.getEndPeriod().isBefore(now)){
                    eventPost.setEventStatus("종료");
                    eventPostRepository.save(eventPost);
                }
            }
        }
    }
    /*
    스케쥴러 config 클래스입니다.
    매일 정각에 행사글 상태가 진행중인 포스트를 뽑아 그중에서 endPeriod가 오늘보다 전이라면 상태를 종료로 바꾸는 스케쥴러입니다.
    */

}
