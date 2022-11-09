package com.team03.godchoice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("USER", "유저"),
    ADMIN("ADMIN", "관리자");

    private final String key;
    private final String title;
}
