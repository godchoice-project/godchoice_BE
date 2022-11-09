package com.team03.godchoice.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String userName;

    private String userAddressTag;

    private String userImgUrl;

    //UUID 난수 암호화
    @Column(nullable = false)
    private String pw;

    //ADMIN , USER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private Boolean isAccepted = false;

    @Column
    private Boolean isDeleted;

    @Column
    private String provider;
    @Column
    private String userRealEmail;



    @Builder
    public Member(String email, String userName, String  userImgUrl,String pw, Boolean isAccepted, Boolean isDeleted, Role role,
                  String userRealEmail, String provider){
        this.email=email;
        this.userName=userName;
        this.userImgUrl=userImgUrl;
        this.pw=pw;
        this.isAccepted=isAccepted;
        this.isDeleted=isDeleted;
        this.role = role;
        this.userRealEmail = userRealEmail;
        this.provider = provider;
    }
}