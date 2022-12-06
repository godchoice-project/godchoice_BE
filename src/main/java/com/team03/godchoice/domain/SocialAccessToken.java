package com.team03.godchoice.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Getter
@Entity
@NoArgsConstructor
public class SocialAccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessId;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String accountEmail;

    @NotBlank
    private String provider;

    public SocialAccessToken(String accessToken, String accountEmail, String provider) {
        this.accessToken = accessToken;
        this.accountEmail = accountEmail;
        this.provider = provider;
    }
}
