package com.nminh.kiemthu.repository;


import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.entity.TeacherSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherSalaryRepository extends JpaRepository<TeacherSalary, Long> {
    Optional<TeacherSalary> findByTeacherIdAndSemesterId(Long teacherId, Long semesterId);
    List<TeacherSalary> findBySemester(Semester semester);
    List<TeacherSalary> findByTeacherId(Long id);
}

