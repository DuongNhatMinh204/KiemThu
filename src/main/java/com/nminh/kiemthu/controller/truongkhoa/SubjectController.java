package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.Subject;
import com.nminh.kiemthu.model.request.SubjectCreateDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.SubjectService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    // TẠO MỚI MÔN HỌC
    @PostMapping("/create")
    public ApiResponse create(@Valid @RequestBody SubjectCreateDTO subjectCreateDTO) {
        log.info("Create subject: {}", subjectCreateDTO);
        ApiResponse apiResponse = new ApiResponse();
        Subject subject = subjectService.createSubject(subjectCreateDTO);
        apiResponse.setData(subject);
        apiResponse.setMessage("Subject created");
        log.info("Subject created: {}", subject);
        return apiResponse;
    }

    // XEM DANH SÁCH MÔN HỌC  CỦA KHOA
    @GetMapping("/view-in-department/{id}")
    public ApiResponse viewSubjectInDepartment(@PathVariable Long id) {
        log.info("View subject in department: {}", id);
        ApiResponse apiResponse = new ApiResponse();
        List<Subject> subjectList = subjectService.getSubjectsInDepartment(id) ;
        apiResponse.setData(subjectList);
        apiResponse.setMessage("Subject viewed in department");
        log.info("Subject viewed in department: {}", subjectList);
        return apiResponse;
    }
    @PutMapping("/change/{id}")
    public ApiResponse change(@PathVariable Long id ,@RequestBody SubjectCreateDTO subjectChangeDTO) {
        log.info("Change subject: {}", id);
        ApiResponse apiResponse = new ApiResponse();
        Subject subject = subjectService.change(id, subjectChangeDTO);
        apiResponse.setData(subject);
        apiResponse.setMessage("Subject changed");
        log.info("Subject changed: {}", subject);
        return apiResponse;
    }
    @DeleteMapping("delete/{id}")
    public ApiResponse delete(@PathVariable Long id) {
        log.info("Delete subject: {}", id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(subjectService.delete(id));
        apiResponse.setMessage("Subject deleted");
        log.info("Subject deleted: {}", id);
        return apiResponse;
    }
}
