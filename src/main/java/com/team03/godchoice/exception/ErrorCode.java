package com.team03.godchoice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //에러코드 쓰는곳
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND.value(), "M001", "유저가 존재하지 않습니다.");


    private final int status;
    private final String code;
    private final String message;
}
