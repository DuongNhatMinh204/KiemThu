package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.model.request.TeacherDTO;

import java.util.List;

public interface TeacherService {
    Teacher createTeacherAccount(TeacherDTO teacherDTO);
    List<Teacher> getAllTeachers();
    List<Teacher> getAllTeachersOfDepartment(Long departmentId);
}
