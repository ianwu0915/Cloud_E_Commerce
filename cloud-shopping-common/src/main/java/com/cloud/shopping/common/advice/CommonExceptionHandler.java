package com.cloud.shopping.common.advice;


import com.cloud.shopping.common.enums.ExceptionEnum;
import com.cloud.shopping.common.exception.LyException;
import com.cloud.shopping.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class is used to handle exceptions thrown by the controller
 * It will return an ExceptionResult object with the status code and message
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> HandlerException(LyException le){
        ExceptionEnum em = le.getExceptionEnum();
        return ResponseEntity.status(le.getExceptionEnum().getCode()).body(new ExceptionResult(le.getExceptionEnum()));
    }
}
