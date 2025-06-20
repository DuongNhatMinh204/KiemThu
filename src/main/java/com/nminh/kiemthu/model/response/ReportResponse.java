package com.nminh.kiemthu.model.response;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.enums.StatusPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportResponse {
    private TeacherResponse teacherResponse;
    private List<ClassRoomResponse> classRoomResponses;
    private double totalHoursTeaching;
    private double totalSalary;
    private StatusPayment statusPayment;
}
