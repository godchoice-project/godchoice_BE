package com.team03.godchoice.domain;

import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.domain.domainenum.Role;
import com.team03.godchoice.dto.requestDto.MyPageReqDto;
import lombok.*;

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

    private RegionTag userAddressTag;

    private String userImgUrl;

    //UUID 난수 암호화
    @Column(nullable = false)
    private String pw;

    @Column
    private String provider;

    //ADMIN , USER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private Boolean isAccepted = false;

    @Column
    private Boolean isDeleted;



    @Builder
    public Member(String email, String userName, String  userImgUrl,String pw, String provider,Boolean isAccepted, Boolean isDeleted, Role role){
        this.email=email;
        this.userName=userName;
        this.userImgUrl=userImgUrl;
        this.pw=pw;
        this.provider = provider;
        this.isAccepted=isAccepted;
        this.isDeleted=isDeleted;
        this.role = role;
    }

    public void update(MyPageReqDto user, RegionTag regionTag, String userImgUrl) {
        this.userName = user.getUserName();
        this.userAddressTag = regionTag;
        this.userImgUrl = userImgUrl;
    }
}