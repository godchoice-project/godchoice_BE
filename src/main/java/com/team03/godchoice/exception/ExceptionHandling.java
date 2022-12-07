package com.team03.godchoice.exception;

import com.team03.godchoice.dto.GlobalResDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler({CustomException.class})
    protected Object handleCustomException(CustomException e) {
        return GlobalResDto.fail(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return GlobalResDto.fail(errorMessage);
    }

    //런타임 예외처리 해야함 (알수없는 오류)
//    @ExceptionHandler(RuntimeException.class)
//    public Object runTimeException(RuntimeException e) {
//        return GlobalResDto.fail(ErrorCode.NOT_FOUND);
//    }
}
