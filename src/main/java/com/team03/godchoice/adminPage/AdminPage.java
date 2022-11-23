package com.team03.godchoice.adminPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AdminPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String postLink;
    @Column(nullable = false)
    private String imgLink;

    public AdminPage(AdminPostReqDto adminPostReqDto, String postLink) {
        this.title = adminPostReqDto.getTitle();
        this.postLink = adminPostReqDto.getPostLink();
        this.imgLink = postLink;
    }
}
