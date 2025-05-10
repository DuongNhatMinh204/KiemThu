package com.nminh.kiemthu.service;

import com.nminh.kiemthu.model.response.TeacherSalaryResponse;

import java.util.List;

public interface TeacherSalaryService {
    TeacherSalaryResponse calculateTeacherSalary(Long teacherId, Long semesterId);
    List<TeacherSalaryResponse> getTeacherSalariesBySemester(Long semesterId);
    TeacherSalaryResponse updatePaymentStatus(Long teacherSalaryId, boolean isPaid);
}