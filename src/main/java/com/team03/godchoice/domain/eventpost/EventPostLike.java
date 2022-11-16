package com.team03.godchoice.domain.eventpost;

import com.team03.godchoice.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class EventPostLike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventPost eventPost;

    public EventPostLike(Member member, EventPost eventPost){
        this.member=member;
        this.eventPost=eventPost;
    }
}
