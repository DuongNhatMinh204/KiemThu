package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.model.request.DepartmentCreateDTO;

public interface DepartmentService {
    Department create(DepartmentCreateDTO departmentCreateDTO);
}
