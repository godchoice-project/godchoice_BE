package com.team03.godchoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.dto.requestDto.GatherPostRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class GatherPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gatherPostId;
    @Column
    private String category;
    @Column
    private String date;
    @Column
    private int number;
    @Column
    private String kakaoLink;
    @Column
    private String sex;
    @Column
    private int startAge;
    @Column
    private int endAge;
    @Column
    private String tittle;
    @Column
    private String content;
    @Column
    private String postLink;
    @Column
    private String postAddress;
    @ManyToOne
    @JoinColumn(name = "memberId")
    @JsonIgnore
    private Member member;


    public GatherPost(GatherPostRequestDto gatherPostDto, Member member) {
        this.category = gatherPostDto.getCategory();
        this.date = gatherPostDto.getDate();
        this.number = gatherPostDto.getNumber();
        this.kakaoLink = gatherPostDto.getKakaoLink();
        this.sex = gatherPostDto.getSex();
        this.startAge = gatherPostDto.getStatAge();
        this.endAge = gatherPostDto.getEndAge();
        this.tittle = gatherPostDto.getTitle();
        this.content = gatherPostDto.getContent();
        this.postLink = gatherPostDto.getPostLink();
        this.postAddress = gatherPostDto.getPostAddress();
        this.member = member;
    }
}
