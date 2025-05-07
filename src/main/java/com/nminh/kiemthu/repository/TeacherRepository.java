package com.nminh.kiemthu.repository;

import com.nminh.kiemthu.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByPhone(String phone);
}
