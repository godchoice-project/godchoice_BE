package com.team03.godchoice.domain.gatherPost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.Timestamped;
import com.team03.godchoice.domain.domainenum.Category;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.dto.requestDto.gatherpostDto.GatherPostRequestDto;
import com.team03.godchoice.dto.requestDto.gatherpostDto.GatherPostUpdateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class GatherPost extends Timestamped {

    @Id
    @Column(name = "gatherpostid")
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
    private String title;

    @Column(length = 5000)
    private String content;

    @Column
    private String postLink;

    @Column
    private String postAddress;
    @OneToMany(mappedBy = "gatherPost", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private final List<GatherPostImg> gatherPostImg = new ArrayList<>();

    @Column(columnDefinition = "integer default 0", nullable = false)
    private long viewCount;

    @ManyToOne
    @JoinColumn(name = "memberid")
    @JsonIgnore
    private Member member;

    @Column(nullable = false)
    private RegionTag regionTag;

    @Column(nullable = false)
    private String postStatus;

    @OneToMany(mappedBy = "gatherPost", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<GatherPostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "gatherPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<GatherPostLike> likes = new ArrayList<>();

    public GatherPost(GatherPostRequestDto gatherPostDto, LocalDate date, RegionTag regionTag, String gatherStatus, Member member) {
        this.category = gatherPostDto.getCategory();
        this.date = date;
        this.number = gatherPostDto.getNumber();
        this.kakaoLink = gatherPostDto.getKakaoLink();
        this.sex = gatherPostDto.getSex();
        this.startAge = gatherPostDto.getStartAge();
        this.endAge = gatherPostDto.getEndAge();
        this.title = gatherPostDto.getTitle();
        this.content = gatherPostDto.getContent();
        this.postLink = gatherPostDto.getPostLink();
        this.postAddress = gatherPostDto.getPostAddress();
        this.member = member;
        this.regionTag = regionTag;
        this.postStatus = gatherStatus;
    }

    public void update(GatherPostUpdateDto gatherPostDto, LocalDate date, RegionTag regionTag, String gatherStatus, Member member) {
        this.category = gatherPostDto.getCategory();
        this.date = date;
        this.number = gatherPostDto.getNumber();
        this.kakaoLink = gatherPostDto.getKakaoLink();
        this.sex = gatherPostDto.getSex();
        this.startAge = gatherPostDto.getStartAge();
        this.endAge = gatherPostDto.getEndAge();
        this.title = gatherPostDto.getTitle();
        this.content = gatherPostDto.getContent();
        this.postLink = gatherPostDto.getPostLink();
        this.postAddress = gatherPostDto.getPostAddress();
        this.member = member;
        this.regionTag = regionTag;
        this.postStatus = gatherStatus;
    }

    public void viewCountUp() {
        this.viewCount++;
    }
}
