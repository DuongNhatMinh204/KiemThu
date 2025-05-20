package com.nminh.kiemthu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "short_name")
    private String shortName ;

    @Column(name = "full_name")
    private String fullName ;

    @OneToMany(mappedBy = "degree")
    @JsonIgnore
    private List<Teacher> teacherList ;

    @Column(name = "degree_coefficient")
    private Double degreeCoefficient;
}
