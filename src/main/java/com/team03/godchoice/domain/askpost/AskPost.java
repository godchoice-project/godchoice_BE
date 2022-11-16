package com.team03.godchoice.domain.askpost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.Timestamped;
import com.team03.godchoice.dto.requestDto.AskPostPutRequestDto;
import com.team03.godchoice.dto.requestDto.AskPostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class AskPost extends Timestamped{

    @Id
    @Column(name = "askpostid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long askPostId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String postAddress;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private long viewCount;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "askPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AskPostImg> askPostImg;

    @OneToMany(mappedBy = "askPost", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<AskPostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "askPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AskPostLike> likes = new ArrayList<>();

    public AskPost(AskPostRequestDto askPostRequestDto, Member member){
        this.title=askPostRequestDto.getTitle();
        this.content=askPostRequestDto.getContent();
        this.postAddress=askPostRequestDto.getPostAddress();
        this.member=member;
    }

    public void updateAskPost(AskPostPutRequestDto askPostPutRequestDto) {
        this.title= askPostPutRequestDto.getTitle();
        this.content= askPostPutRequestDto.getContent();
        this.postAddress=askPostPutRequestDto.getPostAddress();
    }

    public void viewCountUp(){
        this.viewCount++;
    }
}
