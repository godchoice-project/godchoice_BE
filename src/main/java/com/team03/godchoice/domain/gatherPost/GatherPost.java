package com.team03.godchoice.domain.gatherPost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.domainenum.Category;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.dto.requestDto.GatherPostRequestDto;
import com.team03.godchoice.dto.requestDto.GatherPostUpdateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class GatherPost {

    @Id
    @Column(name = "gatherpotid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gatherPostId;
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private int number;
    @Column(nullable = false)
    private String kakaoLink;
    @Column(nullable = false)
    private String sex;
    @Column(nullable = false)
    private int startAge;
    @Column(nullable = false)
    private int endAge;
    @Column(nullable = false)
    private String tittle;
    @Column(nullable = false)
    private String content;
    @Column
    private String postLink;
    @Column
    private String postAddress;

    @OneToMany(mappedBy = "gatherPost") //,fetch=FetchType.LAZY, cascade=CascadeType.ALL
    private final List<GatherPostImg> gatherPostImg = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "memberId")
    @JsonIgnore
    private Member member;
    @Column(nullable = false)
    private RegionTag regionTag;
    @Column(nullable = false)
    private String eventStatus;

    public GatherPost(GatherPostRequestDto gatherPostDto, Category category,LocalDate date, RegionTag regionTag, String eventStatus) {
        this.category = category;
        this.date = date;
        this.number = gatherPostDto.getNumber();
        this.kakaoLink = gatherPostDto.getKakaoLink();
        this.sex = gatherPostDto.getSex();
        this.startAge = gatherPostDto.getStartAge();
        this.endAge = gatherPostDto.getEndAge();
        this.tittle = gatherPostDto.getTitle();
        this.content = gatherPostDto.getContent();
        this.postLink = gatherPostDto.getPostLink();
        this.postAddress = gatherPostDto.getPostAddress();
        this.member = member;
        this.regionTag = regionTag;
        this.eventStatus = eventStatus;
    }

    public void update(GatherPostUpdateDto gatherPostDto, LocalDate date, RegionTag regionTag, String eventStatus, Category category) {
        this.category = category;
        this.date = date;
        this.number = gatherPostDto.getNumber();
        this.kakaoLink = gatherPostDto.getKakaoLink();
        this.sex = gatherPostDto.getSex();
        this.startAge = gatherPostDto.getStartAge();
        this.endAge = gatherPostDto.getEndAge();
        this.tittle = gatherPostDto.getTitle();
        this.content = gatherPostDto.getContent();
        this.postLink = gatherPostDto.getPostLink();
        this.postAddress = gatherPostDto.getPostAddress();
//        this.member = member;
        this.regionTag = regionTag;
        this.eventStatus = eventStatus;
    }
}
