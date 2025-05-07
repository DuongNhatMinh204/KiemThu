package com.nminh.kiemthu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nminh.kiemthu.enums.Degree;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name" , nullable = false)
    private String fullName;

    @Column(name = "birthday", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    @Column(name = "phone",nullable = false)
    private String phone;

    @Column(name = "email" ,nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Degree degree;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<ClassRoom> classRooms;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<TeacherSalary> teacherSalaries;
}
