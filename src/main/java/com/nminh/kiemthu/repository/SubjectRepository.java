package com.nminh.kiemthu.repository;

import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findById(Long id);

    List<Subject> findByDepartment(Department department);
}
