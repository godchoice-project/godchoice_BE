package com.team03.godchoice.SSE;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 1000L * 60 * 60;

    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String id = memberId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        sendToClient(emitter, id, "EventStream Created. [memberId=" + memberId + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(memberId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));

        } catch (IOException exception) {
            emitterRepository.deleteById(eventId);
        }
    }

    @Async
    public void send(Member receiver, AlarmType alarmType, String message, Long articlesId, String title) {

        Notification notification = notificationRepository.save(createNotification(receiver, alarmType, message, articlesId, title));

        String receiverId = String.valueOf(receiver.getMemberId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    NotificationResponseDto notificationResponseDto = new NotificationResponseDto(notification);
                    sendToClient(emitter, eventId, notificationResponseDto);
                }
        );
    }

    private Notification createNotification(Member receiver, AlarmType alarmType, String message,
                                            Long articlesId, String title) {

        return Notification.builder()
                .receiver(receiver)
                .alarmType(alarmType)
                .message(message)
                .articlesId(articlesId)
                .title(title)
                .readState(false) // 현재 읽음상태
                .build();
    }

    public GlobalResDto<?> getAllNotification(Member member) {
        List<Notification> notificationList = notificationRepository.findAllByMemberOrderByIdDesc(member);
        List<NotificationResponseDto> notificationResponseDtoList = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationResponseDto notificationResponseDto = new NotificationResponseDto(notification);
            notificationResponseDtoList.add(notificationResponseDto);
        }
        return GlobalResDto.success(notificationResponseDtoList, "알림리스트를 가져왔습니다.");
    }

    @Transactional
    public GlobalResDto<?> deleteNotification(Member member, Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTICE));
        if (!notification.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION_DELETE);
        }
        notificationRepository.delete(notification);
        return GlobalResDto.success(null, "삭제 성공");
    }

    @Transactional
    public GlobalResDto<?> readNotification( Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTICE));
        notification.changeState();
        notificationRepository.save(notification);
        String post;
        if (notification.getAlarmType().equals(AlarmType.eventPostComment) || notification.getAlarmType().equals(AlarmType.eventPostCommentComment)) {
            post = "/eventposts/";
        }else if(notification.getAlarmType().equals(AlarmType.gatherPostComment) || notification.getAlarmType().equals(AlarmType.gatherPostCommentComment)){
            post = "/gatherposts/";
        }else{
            post = "/askposts/";
        }
        return GlobalResDto.success(null, post+notification.getUrl());
    }

    public Long unreadNotification(Member member) {
        return notificationRepository.countUnReadStateNotifications(member.getMemberId());
    }
}
