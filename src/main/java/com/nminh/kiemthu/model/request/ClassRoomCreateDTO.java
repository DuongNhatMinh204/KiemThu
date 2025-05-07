package com.nminh.kiemthu.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomCreateDTO {
    private int numberOfStudents; // số sinh viên
    private String className;
    private Long semesterId;
    private Long subjectId;
    private Long teacherId;
}
