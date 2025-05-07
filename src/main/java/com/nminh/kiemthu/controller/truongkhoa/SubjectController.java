package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.Subject;
import com.nminh.kiemthu.model.request.SubjectCreateDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.SubjectService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("admin/subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

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
}
