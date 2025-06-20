package com.nminh.kiemthu.service;

import com.nminh.kiemthu.model.response.ReportResponse;
import com.nminh.kiemthu.model.response.TeacherSalaryResponse;

import java.util.List;

public interface TeacherSalaryService {
    TeacherSalaryResponse calculateTeacherSalary(Long teacherId, Long semesterId);
    List<TeacherSalaryResponse> calculateTeacherAllSalaryBySemester(Long semesterId);
    List<TeacherSalaryResponse> calculateTeacherAllSalaryByDepartment(Long departmentId);

    List<TeacherSalaryResponse> getTeacherSalariesBySemester(Long semesterId);
    TeacherSalaryResponse getTeacherSalary(Long teacherId, Long semesterId);
    List<TeacherSalaryResponse> getTeacherAllSalariesBySchoolYear(String year);
    List<TeacherSalaryResponse> getTeacherAllSalariesByDepartment(Long departmentId);
    TeacherSalaryResponse updatePaymentStatus(Long teacherSalaryId, boolean isPaid);
    ReportResponse exportReport(Long teacherSalaryId);
}