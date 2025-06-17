package com.nminh.kiemthu.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTS(1001, "user existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1002, "user not existed", HttpStatus.BAD_REQUEST),
    PASSWORD_ERROR(1003, "password error", HttpStatus.BAD_REQUEST),
    ARGUMENT_NOT_VALID(1004, "argument not null or not blank", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND(1005, "department not found", HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1006, "phone existed", HttpStatus.BAD_REQUEST),
    SEMESTER_NOT_FOUND(1007, "semester not found", HttpStatus.BAD_REQUEST),
    SUBJECT_NOT_FOUND(1008, "subject not found", HttpStatus.BAD_REQUEST),
    TEACHER_NOT_FOUND(1009, "teacher not found", HttpStatus.BAD_REQUEST),
    NOT_EXISTS_SUBJECT_IN_THIS_SEMESTER_OF_DEPARTMENT(1010, "not exists subject in this", HttpStatus.BAD_REQUEST),
    CLASS_NOT_FOUND(1011, "class not found", HttpStatus.BAD_REQUEST),
    NOT_EXISTS_DEGREE(1012, "degree not found", HttpStatus.BAD_REQUEST),
    FULL_NAME_DEGREE_EXISTS(1013, "Full name degree existed", HttpStatus.BAD_REQUEST),
    SHORT_NAME_DEGREE_EXISTS(1014, "Short name degree existed", HttpStatus.BAD_REQUEST),
    TEACHER_SALARY_NOT_FOUND(1015, "Teacher salary not found", HttpStatus.BAD_REQUEST),
    EXISTS_SEMESTER_NAME(1016, "Exists semester name", HttpStatus.BAD_REQUEST),
    TIME_BEGIN_INVALID(1017, "time begin invalid", HttpStatus.BAD_REQUEST),
    TIME_END_INVALID(1018, "time end invalid", HttpStatus.BAD_REQUEST),
    SEMESTER_NAME_NOT_VALID(1019, "semester name not valid", HttpStatus.BAD_REQUEST),
    SCHOOL_YEAR_INVALID(1020, "school year not valid", HttpStatus.BAD_REQUEST),
    DEGREE_NOT_FOUND(1021, "degree not found", HttpStatus.BAD_REQUEST),
    TUITION_NOT_FOUND(1022, "tuition not found", HttpStatus.BAD_REQUEST),
    PAYMENT_STATUS_UPDATE_NOT_ALLOWED(1023, "payment status update not allowed", HttpStatus.BAD_REQUEST),
    SHORT_NAME_NOT_VALID(1024, "short name must not contain digits", HttpStatus.BAD_REQUEST),
    FULL_NAME_NOT_VALID(1025, "full name must not contain digits", HttpStatus.BAD_REQUEST),
    NUMBER_OF_CREDIT_NOT_VALID(1026, "credit number must be > 0", HttpStatus.BAD_REQUEST),
    NUMBER_OF_LESSON_NOT_VALID(1027, "lesson must be > 0", HttpStatus.BAD_REQUEST),
    NUMBER_OF_CLASSES_NOT_VALID(1028, "classes must be > 0", HttpStatus.BAD_REQUEST),
    NUMBER_OF_STUDENT_NOT_VALID(1029, "student must be > 0", HttpStatus.BAD_REQUEST) ,
    MODULE_COEFFICENT_NOT_VALID(1030, "module coefficent must be >= 0", HttpStatus.BAD_REQUEST),
    ;

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}