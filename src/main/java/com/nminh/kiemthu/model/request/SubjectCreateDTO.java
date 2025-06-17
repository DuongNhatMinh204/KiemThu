package com.nminh.kiemthu.model.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCreateDTO {
    private String subjectName;
    private int credits ;
    @Min(value = 0,message = "MODULE_COEFFICENT_NOT_VALID" )
    private Double module_coefficient ;//hệ số học phần
    private Double numberOfLessons ; // số tiết
    private Long departmentId ;
}
