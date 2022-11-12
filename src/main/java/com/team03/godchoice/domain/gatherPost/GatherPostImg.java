package com.team03.godchoice.domain.gatherPost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
public class GatherPostImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gatherPostImgId;
    @Column
    private String imgUrl;
    @ManyToOne
    @JoinColumn(name = "gatherpostid")
    @JsonIgnore
    private GatherPost gatherPost;

    public GatherPostImg(String image, GatherPost gatherPost) {
        this.imgUrl = image;
        this.gatherPost = gatherPost;
    }
}
