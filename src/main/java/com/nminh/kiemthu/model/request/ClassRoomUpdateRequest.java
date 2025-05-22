package com.nminh.kiemthu.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomUpdateRequest {

    private int numberOfStudents; // số sinh viên
    private Long teacherId;
}
