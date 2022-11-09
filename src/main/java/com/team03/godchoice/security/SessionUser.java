package com.team03.godchoice.security;

import com.team03.godchoice.domain.Member;

import java.io.Serializable;

public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String image;

    public SessionUser(Member member) {
        this.name = member.getUserName();
        this.email = member.getEmail();
        this.image = member.getUserImgUrl();
    }

    public SessionUser() {
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImgUrl() {
        return image;
    }

    public void setUserImgUrl(String image) {
        this.image = image;
    }
}