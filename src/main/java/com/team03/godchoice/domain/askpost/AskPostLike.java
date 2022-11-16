package com.team03.godchoice.domain.askpost;

import com.team03.godchoice.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class AskPostLike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private AskPost askPost;

    public AskPostLike(Member member, AskPost askPost){
        this.member=member;
        this.askPost=askPost;
    }

}
