package com.team03.godchoice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //에러코드 쓰는곳
    USER_ERROR(HttpStatus.BAD_REQUEST.value(), "A001","관리자 계정이 아닙니다"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND.value(), "M001", "유저가 존재하지 않습니다."),
    DELETED_USER_EXCEPTION(HttpStatus.BAD_REQUEST.value(),"M002" ,"이미 탈퇴한 계정입니다.\n다른 계정으로 시도해 주세요." ),
    NOT_MATCH_MEMBER(HttpStatus.BAD_REQUEST.value(), "M003","작성자가 일치하지 않습니다."),
    DUPLICATION_NICKNAME(HttpStatus.BAD_REQUEST.value(), "M004","닉네임이 중복되었습니다."),

    NOT_FOUND_POST(HttpStatus.NOT_FOUND.value(), "P001","게시물이 존재하지 않습니다."),
    NO_PERMISSION_DELETE(HttpStatus.BAD_REQUEST.value(), "P002","삭제할 권한이 없습니다."),
    NO_PERMISSION_CHANGE(HttpStatus.BAD_REQUEST.value(), "P003","수정할 권한이 없습니다"),

    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND.value(), "C001","댓글이 존재하지 않습니다."),

    NOT_FOUND_IMG(HttpStatus.NOT_FOUND.value(), "I001","이미지가 존재하지 않습니다." ),
    DATESTATUS_ERROR(HttpStatus.BAD_REQUEST.value(), "D001","만남날짜가 지난 날짜입니다." ),
    COMMENT_ERROR(HttpStatus.BAD_REQUEST.value(), "C001","대댓글의 댓글아이디가 다릅니다." ),

    NOT_FOUND_NOTICE(HttpStatus.NOT_FOUND.value(), "N001","알림이 존재하지 않습니다."),

    ERROR(HttpStatus.NO_CONTENT.value(),"S001","알수업는오류");


    private final int status;
    private final String code;
    private final String message;
}
