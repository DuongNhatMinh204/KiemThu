package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Subject;
import com.nminh.kiemthu.model.request.SubjectCreateDTO;

public interface SubjectService {
    Subject createSubject(SubjectCreateDTO subjectCreateDTO);
}
