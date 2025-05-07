package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.model.request.TeacherDTO;

public interface TeacherService {
    Teacher createTeacherAccount(TeacherDTO teacherDTO);
}
