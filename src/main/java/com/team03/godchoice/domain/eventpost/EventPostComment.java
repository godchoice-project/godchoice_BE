package com.team03.godchoice.domain.eventpost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.Timestamped;
import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.domain.domainenum.DeleteStatus;
import com.team03.godchoice.dto.requestDto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EventPostComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @JoinColumn(nullable = false)
    @JsonIgnore
    @ManyToOne
    private EventPost eventPost;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isDeleted;

    // 상위 댓글
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private EventPostComment parent;

    // 하위 댓글
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @JsonIgnore
    private List<EventPostComment> children = new ArrayList<>();

    public EventPostComment(CommentRequestDto commentRequestDto, EventPost eventPost, Member member, EventPostComment parentComment) {
        this.content = commentRequestDto.getContent();
        this.eventPost = eventPost;
        this.member = member;
        this.parent = parentComment;
        if (this.parent != null) {
            this.parent.getChildren().add(this);
        }
        this.isDeleted = DeleteStatus.N;
    }

    public void changeDeletedStatus(DeleteStatus type) {
        this.isDeleted = type;
    }
}
