package com.nminh.kiemthu.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    USER_EXISTS(1001,"user existed" , HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1002,"user not existed" , HttpStatus.BAD_REQUEST),
    PASSWORD_ERROR(1003,"password error" , HttpStatus.BAD_REQUEST),
    ARGUMENT_NOT_VALID(1004,"argument not null or not blank" , HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND(1005,"department not found" , HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1006,"phone existed" , HttpStatus.BAD_REQUEST),
    SEMESTER_NOT_FOUND(1007,"semester not found" , HttpStatus.BAD_REQUEST),
    SUBJECT_NOT_FOUND(1008,"subject not found" , HttpStatus.BAD_REQUEST),
    TEACHER_NOT_FOUND(1009,"teacher not found" , HttpStatus.BAD_REQUEST),
    NOT_EXISTS_SUBJECT_IN_THIS_SEMESTER_OF_DEPARTMENT(1010,"not exists subject in this " , HttpStatus.BAD_REQUEST),
    CLASS_NOT_FOUND(1011,"class not found" , HttpStatus.BAD_REQUEST),
    NOT_EXISTS_DEGREE(1012, "degree not found", HttpStatus.BAD_REQUEST),
    FULL_NAME_DEGREE_EXISTS(1013, "Full name degree existed", HttpStatus.BAD_REQUEST),
    SHORT_NAME_DEGREE_EXISTS(1014, "Short nmae degree existed", HttpStatus.BAD_REQUEST),
    TEACHER_SALARY_NOT_FOUND(1015, "Teacher salary not found", HttpStatus.BAD_REQUEST),
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
