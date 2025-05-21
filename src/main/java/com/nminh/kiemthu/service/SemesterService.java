package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.model.request.SemesterCreateDTO;

import java.util.List;

public interface SemesterService {
    Semester createSemester(SemesterCreateDTO semesterCreateDTO);
    List<Semester> getAllSemesters();
    Semester updateSemester(Long id,SemesterCreateDTO semesterCreateDTO);
    String deleteSemester(Long id);
}
