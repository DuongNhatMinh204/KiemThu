package com.nminh.kiemthu.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    USER_EXISTS(1001,"user existed" , HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1002,"user not existed" , HttpStatus.BAD_REQUEST),
    PASSWORD_ERROR(1003,"password error" , HttpStatus.BAD_REQUEST),
    ;

    private int code ;
    private String message ;
    private HttpStatusCode httpStatusCode ;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
