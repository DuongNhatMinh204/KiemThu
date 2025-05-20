package com.nminh.kiemthu.model.response;

import com.nminh.kiemthu.entity.Degree;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherResponse {
    private Long id;
    private String name;
    private String department;
    private String email;
    private Degree degree;
}
