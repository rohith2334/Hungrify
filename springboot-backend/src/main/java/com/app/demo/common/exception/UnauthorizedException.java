package com.app.demo.common.exception;


import lombok.Data;

@Data
public class UnauthorizedException extends  RuntimeException{
    private static final long serialVersionUID=1L;

    private String errorCode;
    public UnauthorizedException(String errorCode,String message){
        super(message);
        this.errorCode=errorCode;
    }
}
