package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.model.request.SemesterCreateDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.SemesterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/update/{id}")
    public ApiResponse updateSemester(@PathVariable Long id ,@RequestBody SemesterCreateDTO semesterCreateDTO) {
        log.info("updateSemester");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(semesterService.updateSemester(id,semesterCreateDTO));
        apiResponse.setMessage("Semester updated");
        return apiResponse;
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteSemester(@PathVariable Long id) {
        log.info("deleteSemester");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(semesterService.deleteSemester(id));
        apiResponse.setMessage("Semester deleted");
        return apiResponse;
    }
    @GetMapping("/get-all")
    public ApiResponse getAllSemester() {
        log.info("getAllSemester");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(semesterService.getAllSemesters());
        apiResponse.setMessage("Semester list");
        return apiResponse;
    }
}
