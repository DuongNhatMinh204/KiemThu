package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.model.request.TeacherDTO;
import com.nminh.kiemthu.model.response.InfoTeacherResponseDTO;

import java.util.List;

public interface TeacherService {
    Teacher createTeacherAccount(TeacherDTO teacherDTO);
    List<Teacher> getAllTeachers();
    List<Teacher> getAllTeachersOfDepartment(Long departmentId);
    String deleteTeacherAccount(Long id);
    InfoTeacherResponseDTO getInfoTeacher(Long semesterId ,Long departmentId,Long teacherId) ;
    Teacher updateTeacherAccount(Long id,TeacherDTO teacherDTO);
    Teacher getTeacherById(Long id);
}
