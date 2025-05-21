package com.nminh.kiemthu.repository;

import com.nminh.kiemthu.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Optional<Semester> findById(Long id);

    List<Semester> findBySchoolYear(String schoolYear);
}
