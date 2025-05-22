package com.nminh.kiemthu.entity;

import com.nminh.kiemthu.constants.Constant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String className;

    @Column(name = "number_of_student", nullable = false)
    private int numberOfStudents; // số sinh viên

    @Column(name = "class_coefficient" ,nullable = false)
    private double classCoefficient; // hệ số lớp

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester; // học kỳ

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; // môn học

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private double determineCoeficient(int numberOfStudents){
        if(numberOfStudents>0 && numberOfStudents < 20) {
            return Constant.NUMBER_STUDENT_LESS_THAN_TWENTY ;
        } else if(numberOfStudents>= 20 && numberOfStudents <= 29) {
            return Constant.NUMBER_STUDENT_LESS_THAN_TWENTY_NINE ;
        }else if(numberOfStudents>= 30 && numberOfStudents <= 39) {
            return Constant.NUMBER_STUDENT_LESS_THAN_THIRTY_NINE;
        } else if (numberOfStudents >= 40 && numberOfStudents <= 49) {
            return Constant.NUMBER_STUDENT_LESS_THAN_FOURTY_NINE;
        }else if (numberOfStudents >= 50 && numberOfStudents <= 59) {
            return Constant.NUMBER_STUDENT_LESS_THAN_FIFTY_NINE;
        }else if (numberOfStudents >= 60 && numberOfStudents <= 69) {
            return Constant.NUMBER_STUDENT_LESS_THAN_SIXTY_NINE ;
        }else if (numberOfStudents >= 70 && numberOfStudents <= 79) {
            return Constant.NUMBER_STUDENT_LESS_THAN_SEVENTIES_NINE ;
        }else {
            return 0 ;
        }
    }
    public ClassRoom(int numberOfStudents , Semester semester , Subject subject){
        this.numberOfStudents = numberOfStudents;
        this.semester = semester;
        this.subject = subject;
        this.classCoefficient = determineCoeficient(numberOfStudents);
    }

    public ClassRoom(String className, int numberOfStudents, Semester semester, Subject subject,Teacher teacher) {
        this.className = className;
        this.numberOfStudents = numberOfStudents;
        this.classCoefficient = determineCoeficient(numberOfStudents);
        this.semester = semester;
        this.subject = subject;
        this.teacher = teacher;
    }
}
