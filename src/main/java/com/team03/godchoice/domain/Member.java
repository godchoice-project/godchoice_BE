package com.team03.godchoice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member{

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
    private String role;
}
