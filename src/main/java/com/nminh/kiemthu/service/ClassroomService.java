package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;

public interface ClassroomService {
    ClassRoom createClassroom(ClassRoomCreateDTO classRoomCreateDTO);
}
