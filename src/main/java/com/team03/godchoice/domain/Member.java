package com.team03.godchoice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

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
    @Column(nullable = false)
    private String role = "ROLE_USER";

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