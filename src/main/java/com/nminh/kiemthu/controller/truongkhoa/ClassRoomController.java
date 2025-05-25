package com.nminh.kiemthu.controller.truongkhoa;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;
import com.nminh.kiemthu.model.request.ClassRoomUpdateRequest;
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
        List<ClassRoom> classRooms= classroomService.createClassroom(classRoomCreateDTO);
        response.setData(classRooms);
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

    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteClassRoom(@PathVariable Long id) {
        log.info("ClassRoomController.deleteClassRoom");
        ApiResponse response = new ApiResponse();
        response.setData(classroomService.deleteClassroom(id));
        response.setMessage("ClassRoom deleted");
        return response;
    }

    @PutMapping("/change/{id}")
    public ApiResponse changeClassRoom(@PathVariable Long id , @RequestBody ClassRoomUpdateRequest classRoomUpdateRequest) {
        log.info("ClassRoomController.changeClassRoom");
        ApiResponse response = new ApiResponse();
        response.setData(classroomService.changeClassRoom(id, classRoomUpdateRequest));
        response.setMessage("ClassRoom changed");
        return response;
    }
    @GetMapping("/by-semester-id/{semesterId}")
    public ApiResponse getClassRoomsBySemesterId(@PathVariable Long semesterId) {
        log.info("ClassRoomController.getClassRoomsBySemesterId with semesterId: {}", semesterId);
        ApiResponse response = new ApiResponse();
        List<ClassRoom> classRooms = classroomService.findClassRoomsBySemesterId(semesterId);
        response.setData(classRooms);
        response.setMessage("ClassRooms retrieved by semesterId");
        return response;
    }

    @GetMapping("/by-semester-name/{semesterName}")
    public ApiResponse getClassRoomsBySemesterName(@PathVariable String semesterName) {
        log.info("ClassRoomController.getClassRoomsBySemesterName with semesterName: {}", semesterName);
        ApiResponse response = new ApiResponse();
        List<ClassRoom> classRooms = classroomService.findClassRoomsBySemesterName(semesterName);
        response.setData(classRooms);
        response.setMessage("ClassRooms retrieved by semesterName");
        return response;
    }

    @GetMapping("/by-semester-id-and-teacher-id")
    public ApiResponse getClassRoomsBySemesterIdAndTeacherId(
            @RequestParam Long semesterId,
            @RequestParam Long teacherId) {
        log.info("ClassRoomController.getClassRoomsBySemesterIdAndTeacherId with semesterId: {}, teacherId: {}", semesterId, teacherId);
        ApiResponse response = new ApiResponse();
        List<ClassRoom> classRooms = classroomService.findClassRoomsBySemesterIdAndTeacherId(semesterId, teacherId);
        response.setData(classRooms);
        response.setMessage("ClassRooms retrieved by semesterId and teacherId");
        return response;
    }
}
