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
@RequestMapping("/admin/teacher")
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

    @GetMapping("/get-all-of-department/{id}")
    public ApiResponse getAllOfDepartment(@PathVariable Long id){
        log.info("getAllOfDepartment ");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(teacherService.getAllTeachersOfDepartment(id));
        log.info("getAllOfDepartment ");
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteTeacherAccount(@PathVariable Long id){
        log.info("deleteTeacherAccount ");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(teacherService.deleteTeacherAccount(id));
        log.info("deleteTeacherAccount ");
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse updateTeacherAccount(@PathVariable Long id,@RequestBody TeacherDTO teacherDTO){
        log.info("updateTeacherAccount ");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(teacherService.updateTeacherAccount(id, teacherDTO));
        log.info("updateTeacherAccount ");
        return apiResponse;
    }

    //Thống kê tổng số tiết giảng , số lớp đã dạy , tiền dạy của từng giảng viên theo từng kỳ
    @GetMapping("/getList")
    public ApiResponse getListOfTeacherAccount(@RequestParam Long semesterId , @RequestParam Long departmentId ,
                                               @RequestParam Long teacherId ){
        log.info("getListOfTeacherAccount ");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(teacherService.getInfoTeacher(semesterId,departmentId,teacherId));
        apiResponse.setMessage("Success ");
        log.info("getListOfTeacherAccount ");
        return apiResponse;
    }
    @GetMapping("/get/{id}")
    public ApiResponse getTeacherAccount(@PathVariable Long id){
        log.info("getTeacherAccount ");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(teacherService.getTeacherById(id));
        log.info("getTeacherAccount ");
        return apiResponse;
    }
}
