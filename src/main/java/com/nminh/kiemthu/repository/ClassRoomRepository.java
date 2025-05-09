package com.nminh.kiemthu.repository;

import com.nminh.kiemthu.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    // Tìm ClassRoom theo semesterId
    List<ClassRoom> findBySemesterId(Long semesterId);

    // Tìm ClassRoom theo semesterName
    @Query("SELECT cr FROM ClassRoom cr WHERE cr.semester.semesterName = :semesterName")
    List<ClassRoom> findBySemesterName(@Param("semesterName") String semesterName);
}
