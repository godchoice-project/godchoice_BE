package com.team03.godchoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.dto.requestDto.AskPostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class AskPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long askPostId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "askPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Image> image;

    public AskPost(AskPostRequestDto askPostRequestDto, Member member){
        this.title=askPostRequestDto.getTitle();
        this.content=askPostRequestDto.getContent();
        this.member=member;
    }
}
