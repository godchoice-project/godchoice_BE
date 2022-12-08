package com.team03.godchoice.exception;

import com.team03.godchoice.dto.GlobalResDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler({CustomException.class})
    protected Object handleCustomException(CustomException e) {
        return GlobalResDto.fail(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return GlobalResDto.fail(errorMessage);
    }

    //런타임 예외처리 해야함 (알수없는 오류)
    @ExceptionHandler(RuntimeException.class)
    public Object runTimeException(RuntimeException e) {
        return GlobalResDto.fail(ErrorCode.ERROR);
    }
}
