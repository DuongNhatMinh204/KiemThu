package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.model.request.SemesterCreateDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.SemesterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/semester")
@Slf4j
public class SemesterController {
    @Autowired
    private SemesterService semesterService;

    @PostMapping("/create")
    public ApiResponse createSemester(@RequestBody SemesterCreateDTO semesterCreateDTO) {
        log.info("createSemester");
        ApiResponse apiResponse = new ApiResponse();
        Semester semester = semesterService.createSemester(semesterCreateDTO);
        apiResponse.setData(semester);
        apiResponse.setMessage("Semester created");
        return apiResponse;
    }
}
