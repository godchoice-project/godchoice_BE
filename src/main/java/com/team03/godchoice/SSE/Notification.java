package com.team03.godchoice.SSE;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean readState;

    @Column(nullable = false)
    private Long url;

    @ManyToOne
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_member_id")
    private Member member;

    @Column
    private String title;

    @Builder
    public Notification(AlarmType alarmType, String message, Boolean readState,
                        Long articlesId, Member receiver, String title) {
        this.alarmType = alarmType;
        this.message = message;
        this.readState = readState;
        this.url = articlesId;
        this.member = receiver;
        this.title = title;
    }

    public void changeState() {
        readState = true;
    }

}
