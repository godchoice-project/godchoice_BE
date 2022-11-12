package com.team03.godchoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.godchoice.domain.askpost.AskPost;
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
public class Comment {
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
    private Comment parent;

    // 하위 댓글
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public Comment(CommentRequestDto commentRequestDto, AskPost askPost, Member member, Comment parentComment) {
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
