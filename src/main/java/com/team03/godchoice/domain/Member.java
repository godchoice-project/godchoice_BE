package com.team03.godchoice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Timestamped{

    @Id
    @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String userName;

    private String address;

    private String userTag;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private String userImgUrl;

    @Column(nullable = false)
    private String pw;
}
