package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.model.request.DepartmentCreateDTO;

import java.util.List;

public interface DepartmentService {
    Department create(DepartmentCreateDTO departmentCreateDTO);
    List<Department> getAllDepartments();
}
