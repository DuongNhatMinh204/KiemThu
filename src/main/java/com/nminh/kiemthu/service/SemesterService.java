package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.model.request.SemesterCreateDTO;

public interface SemesterService {
    Semester createSemester(SemesterCreateDTO semesterCreateDTO);
}
