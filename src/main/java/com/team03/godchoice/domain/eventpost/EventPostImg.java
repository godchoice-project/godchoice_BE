package com.team03.godchoice.domain.eventpost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostImg {

    @Id
    @Column(name = "eventpostimgid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventPostImgId;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "eventpostid")
    private EventPost eventPost;

    @Column(nullable = false)
    private String imgUrl;

    public EventPostImg(String img, EventPost eventPost) {
        this.eventPost = eventPost;
        this.imgUrl = img;
    }
}
