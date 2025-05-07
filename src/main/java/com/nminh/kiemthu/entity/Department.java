package com.nminh.kiemthu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Subject> subjects;
}
