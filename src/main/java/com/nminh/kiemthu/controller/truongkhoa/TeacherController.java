package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.model.request.TeacherDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.TeacherService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("admin/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @PostMapping("/create")
    public ApiResponse createTeacherAccount(@Valid @RequestBody TeacherDTO teacherDTO){
        log.info("creating TeacherAccount ");
        ApiResponse apiResponse = new ApiResponse();
        Teacher teacher = teacherService.createTeacherAccount(teacherDTO);
        apiResponse.setData(teacher);

        log.info("created TeacherAccount ");
        return apiResponse;
    }

    @GetMapping("/get-all")
    public ApiResponse getAllTeacherAccount(){
        log.info("getAllTeacherAccount ");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(teacherService.getAllTeachers());
        log.info("getAllTeacherAccount ");
        return apiResponse;
    }

    @GetMapping("get-all-of-department/{id}")
    public ApiResponse getAllOfDepartment(@PathVariable Long id){
        log.info("getAllOfDepartment ");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(teacherService.getAllTeachersOfDepartment(id));
        log.info("getAllOfDepartment ");
        return apiResponse;
    }
}
