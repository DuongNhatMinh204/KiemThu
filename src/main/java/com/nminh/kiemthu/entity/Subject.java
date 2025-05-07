package com.nminh.kiemthu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//  ---- MÔN HỌC----
@Entity
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "credits", nullable = false)
    private int credits; // số tín chỉ

    @Column(name = "module_coefficient" ,nullable = false)
    private double module_coefficient ;//hệ số học phần

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "subject")
    @JsonIgnore
    private List<ClassRoom> classRooms = new ArrayList<>();
}
