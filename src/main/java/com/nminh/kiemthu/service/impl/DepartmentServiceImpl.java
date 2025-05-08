package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.model.request.DepartmentCreateDTO;
import com.nminh.kiemthu.repository.DepartmentRepository;
import com.nminh.kiemthu.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department create(DepartmentCreateDTO departmentCreateDTO) {
        Department department = new Department();

        department.setFullName(departmentCreateDTO.getFullName());
        department.setDescription(departmentCreateDTO.getDescription());
        department.setShortName(departmentCreateDTO.getShortName());

        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
