package com.team03.godchoice.SSE;

import com.team03.godchoice.abstrctPackage.TimeCountClass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NotificationResponseDto {

    private Long notificationId;

    private String message;

    private Long articlesId;

    private Boolean readStatus;

    private AlarmType alarmType;

    private String title;

    private String createdAt;


    @Builder
    public NotificationResponseDto(Long id, String message, Long articlesId, Boolean readStatus,
                                   AlarmType alarmType, String title,String createdAt) {
        this.notificationId = id;
        this.message = message;
        this.articlesId = articlesId;
        this.readStatus = readStatus;
        this.title = title;
        this.alarmType = alarmType;
        this.createdAt = createdAt;
    }

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.message = notification.getMessage();
        this.articlesId = notification.getUrl();
        this.readStatus = notification.getReadState();
        this.title = notification.getTitle();
        this.alarmType = notification.getAlarmType();
        this.createdAt = TimeCountClass.countTime(notification.getCreatedAt());
    }
}