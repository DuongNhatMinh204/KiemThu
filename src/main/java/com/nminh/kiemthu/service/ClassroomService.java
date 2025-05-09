package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;

import java.util.List;

public interface ClassroomService {
    ClassRoom createClassroom(ClassRoomCreateDTO classRoomCreateDTO);
    List<ClassRoom> getListClassrooms(Long departmentId , Long semesterId);
    String deleteClassroom(Long classroomId);
    ClassRoom changeClassRoom(Long id , ClassRoomCreateDTO classRoomChangeDTO);
}
