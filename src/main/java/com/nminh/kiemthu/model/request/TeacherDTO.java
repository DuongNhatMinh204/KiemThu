package com.nminh.kiemthu.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nminh.kiemthu.entity.Degree;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {
    @NotNull(message = "ARGUMENT_NOT_VALID")
    private String fullName ;

    @NotNull(message = "ARGUMENT_NOT_VALID")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    @NotNull(message = "ARGUMENT_NOT_VALID")
    private String email ;

    @NotNull(message = "ARGUMENT_NOT_VALID")
    private String phone;

    @NotNull(message = "ARGUMENT_NOT_VALID")
    private Long  degreeId;

    @NotNull(message = "ARGUMENT_NOT_VALID")
    private Long departmentId;

}
