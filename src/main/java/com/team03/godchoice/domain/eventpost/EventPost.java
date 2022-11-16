package com.team03.godchoice.domain.eventpost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.Timestamped;
import com.team03.godchoice.domain.domainenum.Category;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.dto.requestDto.eventpostDto.EventPostPutReqDto;
import com.team03.godchoice.dto.requestDto.eventpostDto.EventPostReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPost extends Timestamped {

    @Id
    @Column(name = "eventpostid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventPostId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    @JsonIgnore
    private Member member;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private LocalDate startPeriod;

    @Column(nullable = false)
    private LocalDate endPeriod;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String content;

    private String postLink;

    @Column(nullable = false)
    private String postAddress;

    @Column(nullable = false)
    private RegionTag regionTag;

    @Column(nullable = false)
    private String eventStatus;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private long viewCount;

    @OneToMany(mappedBy = "eventPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EventPostImg> postImgUrl = new ArrayList<>();

    @OneToMany(mappedBy = "eventPost", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<EventPostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "eventPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EventPostLike> likes = new ArrayList<>();

    public EventPost(EventPostReqDto eventPostReqDto, Member member, LocalDate startPeriod, LocalDate endPeriod, RegionTag regionTag, String eventStatus) {
        this.member = member;
        this.category = eventPostReqDto.getCategory();
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.title = eventPostReqDto.getTitle();
        this.content = eventPostReqDto.getContent();
        this.postLink = eventPostReqDto.getPostLink();
        this.postAddress = eventPostReqDto.getPostAddress();
        this.regionTag = regionTag;
        this.eventStatus = eventStatus;
    }

    public void update(EventPostPutReqDto eventPostPutReqDto, Member member, LocalDate startPeriod, LocalDate endPeriod, RegionTag regionTag, String eventStatus) {
        this.category = eventPostPutReqDto.getCategory();
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.title = eventPostPutReqDto.getTitle();
        this.content = eventPostPutReqDto.getContent();
        this.postLink = eventPostPutReqDto.getPostLink();
        this.postAddress = eventPostPutReqDto.getPostAddress();
        this.regionTag = regionTag;
        this.eventStatus = eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public void viewCountUp(){
        this.viewCount++;
    }
}
