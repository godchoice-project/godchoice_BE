package com.team03.godchoice.domain.gatherPost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.Timestamped;
import com.team03.godchoice.enumclass.DeleteStatus;
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
public class GatherPostComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @JoinColumn(nullable = false)
    @JsonIgnore
    @ManyToOne
    private GatherPost gatherPost;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isDeleted;

    // 상위 댓글
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private GatherPostComment parent;

    // 하위 댓글
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @JsonIgnore
    private List<GatherPostComment> children = new ArrayList<>();

    public GatherPostComment(CommentRequestDto commentRequestDto, GatherPost gatherPost, Member member, GatherPostComment parentComment) {
        this.content = commentRequestDto.getContent();
        this.gatherPost = gatherPost;
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
