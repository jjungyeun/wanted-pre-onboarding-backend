package com.wjy.wantedpreonboarding.exception;

public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode, Throwable cause){
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public CustomException(String message,ErrorCode errorCode, Throwable cause){
        super(errorCode.getMessage()+message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
