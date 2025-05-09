package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.model.request.DepartmentCreateDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @PostMapping("/create")
    public ApiResponse createDepartment(@Valid @RequestBody DepartmentCreateDTO departmentCreateDTO) {
        log.info("Create department {}", departmentCreateDTO);
        ApiResponse apiResponse = new ApiResponse();
        Department department = departmentService.create(departmentCreateDTO);
        apiResponse.setMessage("Department created");
        apiResponse.setData(department);
        return apiResponse;
    }

    @GetMapping("/getAll")
    public ApiResponse getAllDepartments() {
        ApiResponse apiResponse = new ApiResponse();
        List<Department> departments = departmentService.getAllDepartments();
        apiResponse.setData(departments);
        return apiResponse;
    }
}
