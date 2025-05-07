package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.model.request.TeacherDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.TeacherService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
