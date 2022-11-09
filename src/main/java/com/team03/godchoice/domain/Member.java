package com.team03.godchoice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @Builder
    public Member(String email, String userName, String  userImgUrl,String pw, Boolean isAccepted, Boolean isDeleted, Role role){
        this.email=email;
        this.userName=userName;
        this.userImgUrl=userImgUrl;
        this.pw=pw;
        this.isAccepted=isAccepted;
        this.isDeleted=isDeleted;
        this.role = role;
    }

    //========================================================================
    public Member(String name, String email, String picture) {
        this.userName = name;
        this.email = email;
        this.userImgUrl = picture;
    }

    public Member update(String name, String picture) {
        this.userName = name;
        this.userImgUrl = picture;

        return this;
    }
}