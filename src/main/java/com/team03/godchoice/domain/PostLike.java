package com.team03.godchoice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostLike {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private AskPost askPost;

    public PostLike(Member member, AskPost askPost){
        this.member=member;
        this.askPost=askPost;
    }

}
