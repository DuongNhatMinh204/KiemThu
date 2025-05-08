package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/classroom")
@Slf4j
public class ClassRoomController {
    @Autowired
    private ClassroomService classroomService;

    // TẠO MỚI LỚP HỌC PHẦN
    @PostMapping("/create")
    public ApiResponse createClassRoom(@Valid @RequestBody ClassRoomCreateDTO classRoomCreateDTO) {
        log.info("ClassRoomController.createClassRoom");
        ApiResponse response = new ApiResponse();
        ClassRoom classRoom= classroomService.createClassroom(classRoomCreateDTO);
        response.setData(classRoom);
        response.setMessage("ClassRoom created");
        return response;
    }
    // xem danh sách các lớp học phần do khoa phụ trách trong từng kỳ học.
    @GetMapping("/view")
    public ApiResponse viewClassRoom(@RequestParam Long departmentId , @RequestParam Long semesterId) {
        log.info("ClassRoomController.viewClassRoom");
        ApiResponse response = new ApiResponse();
        List<ClassRoom> res = classroomService.getListClassrooms(departmentId, semesterId);
        response.setData(res);
        response.setMessage("ClassRoom viewed");
        return response;

    }
}
