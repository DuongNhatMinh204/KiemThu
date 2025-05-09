package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Subject;
import com.nminh.kiemthu.model.request.SubjectCreateDTO;

import java.util.List;

public interface SubjectService {
    Subject createSubject(SubjectCreateDTO subjectCreateDTO);
    List<Subject> getSubjectsInDepartment(Long departmentId);
    Subject change(Long id ,SubjectCreateDTO subjectChangeDTO);
    String delete(Long id);
}
