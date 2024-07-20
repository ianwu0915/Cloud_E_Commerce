package com.cloud.shopping.common.exception;

import com.cloud.shopping.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LyException extends RuntimeException{
    private ExceptionEnum exceptionEnum;
}
