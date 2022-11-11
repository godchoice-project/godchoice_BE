package com.team03.godchoice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //에러코드 쓰는곳
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND.value(), "M001", "유저가 존재하지 않습니다."),

    NOT_FOUND_POST(HttpStatus.NOT_FOUND.value(), "M002", "게시물을 찾을 수 없습니다."),
    DELETED_USER_EXCEPTION(HttpStatus.BAD_REQUEST.value(),"M002" ,"이미 탈퇴한 계정입니다.\n다른 계정으로 시도해 주세요." ),

    NOT_FOUND_EVENTPOST(HttpStatus.NOT_FOUND.value(), "P001","게시물이 존재하지 않습니다."),
    NO_PERMISSION_DELETE(HttpStatus.BAD_REQUEST.value(), "P002","게시물을 삭제할 권한이 없습니다."),
    NO_PERMISSION_CHANGE(HttpStatus.BAD_REQUEST.value(), "P003","게시물을 수정할 권한이 없습니다");

    DELETED_USER_EXCEPTION(HttpStatus.BAD_REQUEST.value(),"A001" ,"이미 탈퇴한 계정입니다.\n다른 계정으로 시도해 주세요." ),

    EXPIRE_REFRESH_TOKEN(HttpStatus.FORBIDDEN.value(),"R001" ,"refresh token이 유효하지 않습니다." ),
    NOT_EXIST_REFRESHTOKEN(HttpStatus.BAD_REQUEST.value(), "R002","올바른 RefreshToken을 헤더에 넣어주세요." ),

    //모집글
    GATHERPOST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "GP001", "모집글이 존재 하지 않습니다.");
    private final int status;
    private final String code;
    private final String message;
}
