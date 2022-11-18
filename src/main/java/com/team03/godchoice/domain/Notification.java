//package com.team03.godchoice.domain;
//
//import lombok.*;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//import javax.persistence.*;
//
//@Getter
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EqualsAndHashCode(of = "id")
//public class Notification   {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "notification_id")
//    private Long id;
//
//    @Embedded
//    private String content;
//
//    @Embedded
//    private String  url;
//
//    @Column(nullable = false)
//    private Boolean isRead;
//
//    @Embedded
//    private String  notificationType;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Member receiver;
//
//    @Builder
//    public Notification(Member receiver, String  notificationType, String content, String url, Boolean isRead) {
//        this.receiver = receiver;
//        this.notificationType = notificationType;
//        this.content =  content;
//        this.url = url;
//        this.isRead = isRead;
//    }
//}
