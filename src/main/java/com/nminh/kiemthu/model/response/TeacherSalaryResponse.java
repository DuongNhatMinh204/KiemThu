package com.nminh.kiemthu.model.response;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.enums.StatusPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSalaryResponse {
    private Long id;
    private TeacherResponse teacherResponse;
    private List<String> classRoom;
    private double totalHoursTeaching;
    private double totalSalary;
    private StatusPayment statusPayment;
}
