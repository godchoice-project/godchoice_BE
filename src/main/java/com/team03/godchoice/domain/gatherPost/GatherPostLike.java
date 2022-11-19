package com.team03.godchoice.domain.gatherPost;

import com.team03.godchoice.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class GatherPostLike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private GatherPost gatherPost;

    public GatherPostLike(Member member, GatherPost gatherPost){
        this.member=member;
        this.gatherPost=gatherPost;
    }
}
