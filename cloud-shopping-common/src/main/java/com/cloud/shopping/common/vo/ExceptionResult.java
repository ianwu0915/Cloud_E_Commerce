package com.cloud.shopping.common.vo;

import com.cloud.shopping.common.enums.ExceptionEnum;
import lombok.Data;

@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    // Construct an ExceptionResult object based on the ExceptionEnum object
    public ExceptionResult(ExceptionEnum em){
        this.status=em.getCode();
        this.message=em.getMsg();
        this.timestamp=System.currentTimeMillis();

    }
}
