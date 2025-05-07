package com.nminh.kiemthu.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCreateDTO {
    private String subjectName;
    private int credits ;
    private double module_coefficient ;//hệ số học phần
    private Long departmentId ;
}
