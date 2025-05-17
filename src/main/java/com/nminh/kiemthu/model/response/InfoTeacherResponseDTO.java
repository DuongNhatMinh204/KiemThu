package com.nminh.kiemthu.model.response;

import com.nminh.kiemthu.entity.Degree;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InfoTeacherResponseDTO {
    private String fullName ;
    private String email ;
    private String phoneNumber ;
    private Degree degree ;
    private int numberOfLessons ; // số tiết giảng dạy
    private int numberOfClass ; // số lớp giảng dạy
    private Double total_money ;
}
