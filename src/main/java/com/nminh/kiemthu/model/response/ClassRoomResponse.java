package com.nminh.kiemthu.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassRoomResponse {

    private String className;

    private int numberOfStudents;

    private double classCoefficient;

    private String semesterName ;

    private String schoolYear ;

    private String subject;

}
