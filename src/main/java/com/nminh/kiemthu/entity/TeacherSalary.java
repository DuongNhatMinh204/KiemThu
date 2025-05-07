package com.nminh.kiemthu.entity;

import com.nminh.kiemthu.enums.StatusPayment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(name = "total_hours_teach")
    private double totalHoursTeaching ;

    @Column(name = "total_salary")
    private double totalSalary;

    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;

}
