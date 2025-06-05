package com.nminh.kiemthu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tuition")
@Getter
@Setter
public class Tuition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "money")
    private Long money;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;
}
