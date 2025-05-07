package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.TeacherDTO;
import com.nminh.kiemthu.repository.DepartmentRepository;
import com.nminh.kiemthu.repository.TeacherRepository;
import com.nminh.kiemthu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Teacher createTeacherAccount(TeacherDTO teacherDTO) {
        Department department = departmentRepository.findById(teacherDTO.getDepartmentId())
                .orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        boolean exist = teacherRepository.existsByPhone(teacherDTO.getPhone()) ;
        if(exist) {
            throw new  AppException(ErrorCode.PHONE_EXISTS) ;
        }
        Teacher teacher = new Teacher();

        teacher.setFullName(teacherDTO.getFullName());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhone(teacherDTO.getPhone());
        teacher.setBirthday(teacherDTO.getBirthday());
        teacher.setDegree(teacherDTO.getDegree());
        teacher.setDepartment(department);

        return teacherRepository.save(teacher);

    }
}
