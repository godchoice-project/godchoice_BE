package com.team03.godchoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class AskPostImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "askPostId")
    @JsonIgnore
    private AskPost askPost;

    public AskPostImg(String image, AskPost askPost){
        this.image=image;
        this.askPost=askPost;
    }
}
