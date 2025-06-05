package com.nminh.kiemthu.repository;

import com.nminh.kiemthu.entity.Tuition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TuitionRepository extends JpaRepository<Tuition, Long> {
    Optional<Tuition> findBySemesterId(Long semesterId);
}