package com.nminh.kiemthu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

//  ---- HỌC Kỳ ----
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "semester_name" ,nullable = false)
    private String semesterName ;

    @Column(name = "school_year",nullable = false)
    private String schoolYear ;

    @Column(name = "time_begin", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate timeBegin ;

    @Column(name = "time_end", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate timeEnd ;

    @OneToMany(mappedBy = "semester")
    @JsonIgnore
    private List<ClassRoom> classRooms ;

    @OneToMany(mappedBy = "semester")
    @JsonIgnore
    private List<TeacherSalary> teacherSalaries ;
}
