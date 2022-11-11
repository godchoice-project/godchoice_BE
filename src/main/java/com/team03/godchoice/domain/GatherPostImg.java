package com.team03.godchoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Builder @Data
@AllArgsConstructor
public class GatherPostImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    @Column
    private String image;
    @ManyToOne
    @JoinColumn(name = "gatherPostId")
    @JsonIgnore
    private GatherPost gatherPost;

    public GatherPostImg(String image, GatherPost gatherPost) {
        this.image = image;
        this.gatherPost = gatherPost;
    }
}
