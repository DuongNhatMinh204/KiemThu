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

import java.util.List;

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

    @Override
    public List<Subject> getSubjectsInDepartment(Long departmentId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        return subjectRepository.findByDepartment(department);

    }

    @Override
    public Subject change( Long id,SubjectCreateDTO subjectChangeDTO) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        Department department = departmentRepository.findById(subjectChangeDTO.getDepartmentId())
                        .orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        subject.setSubjectName(subjectChangeDTO.getSubjectName());
        subject.setCredits(subjectChangeDTO.getCredits());
        subject.setModule_coefficient(subjectChangeDTO.getModule_coefficient());
        subject.setDepartment(department);
        return subjectRepository.save(subject);
    }

    @Override
    public String delete(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        subjectRepository.delete(subject);
        return "Subject deleted";
    }
}
