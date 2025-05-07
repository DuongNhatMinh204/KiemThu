package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.entity.Subject;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.SubjectCreateDTO;
import com.nminh.kiemthu.repository.DepartmentRepository;
import com.nminh.kiemthu.repository.SubjectRepository;
import com.nminh.kiemthu.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Subject createSubject(SubjectCreateDTO subjectCreateDTO) {
        Department department = departmentRepository.findById(subjectCreateDTO.getDepartmentId())
                .orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND)) ;

        Subject subject = new Subject();

        subject.setSubjectName(subjectCreateDTO.getSubjectName());
        subject.setCredits(subjectCreateDTO.getCredits());
        subject.setModule_coefficient(subjectCreateDTO.getModule_coefficient());
        subject.setDepartment(department);

        return subjectRepository.save(subject);
    }
}
