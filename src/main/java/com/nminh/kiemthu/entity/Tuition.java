package com.nminh.kiemthu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tuition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tuition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "money")
    private Long money;
    @Column(name = "pre_money")
    private Long pre_money;
    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;
}
