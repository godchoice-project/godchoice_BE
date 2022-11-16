package com.team03.godchoice.domain.askpost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.domain.Member;
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
public class AskPostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @JoinColumn(nullable = false)
    @JsonIgnore
    @ManyToOne
    private AskPost askPost;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isDeleted;

    // 상위 댓글
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private AskPostComment parent;

    // 하위 댓글
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<AskPostComment> children = new ArrayList<>();

    public AskPostComment(CommentRequestDto commentRequestDto, AskPost askPost, Member member, AskPostComment parentComment) {
        this.content = commentRequestDto.getContent();
        this.askPost = askPost;
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
