package com.nminh.kiemthu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//  ---- MÔN HỌC----
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "credits", nullable = false)
    private int credits; // số tín chỉ

    @Column(name = "module_coefficient" ,nullable = false)
    private Double module_coefficient;//hệ số học phần

    @Column(name = "number_of_lessons")
    private Double numberOfLessons;  // số tiết học

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "subject")
    @JsonIgnore
    private List<ClassRoom> classRooms = new ArrayList<>();
}
