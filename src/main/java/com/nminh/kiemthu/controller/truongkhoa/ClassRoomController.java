package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/classroom")
@Slf4j
public class ClassRoomController {
    @Autowired
    private ClassroomService classroomService;
    @PostMapping("/create")
    public ApiResponse createClassRoom(@Valid @RequestBody ClassRoomCreateDTO classRoomCreateDTO) {
        log.info("ClassRoomController.createClassRoom");
        ApiResponse response = new ApiResponse();
        ClassRoom classRoom= classroomService.createClassroom(classRoomCreateDTO);
        response.setData(classRoom);
        response.setMessage("ClassRoom created");
        return response;

    }

}
