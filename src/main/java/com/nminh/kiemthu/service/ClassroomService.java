package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;

import java.util.List;

public interface ClassroomService {
    ClassRoom createClassroom(ClassRoomCreateDTO classRoomCreateDTO);
    public List<ClassRoom> findClassRoomsBySemesterId(Long semesterId);

    // 2. Tìm kiếm ClassRoom theo SemesterName
    public List<ClassRoom> findClassRoomsBySemesterName(String semesterName);
}
