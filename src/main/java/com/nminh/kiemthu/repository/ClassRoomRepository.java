package com.nminh.kiemthu.repository;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.entity.Department;
import com.nminh.kiemthu.entity.Semester;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    @Query("SELECT c FROM ClassRoom c " +
            "WHERE c.semester.id = :semesterId AND c.subject.department.id = :departmentId")
    List<ClassRoom> findByDepartmentAndSemester(@Param("departmentId") Long departmentId, @Param("semesterId") Long semesterId);
    // Tìm ClassRoom theo semesterId
    List<ClassRoom> findBySemesterId(Long semesterId);

    // Tìm ClassRoom theo semesterName
    @Query("SELECT cr FROM ClassRoom cr WHERE cr.semester.semesterName = :semesterName")
    List<ClassRoom> findBySemesterName(@Param("semesterName") String semesterName);
    @Query("SELECT cr FROM ClassRoom cr WHERE cr.semester.id = :semesterId AND cr.teacher.id = :teacherId")
    List<ClassRoom> findBySemesterIdAndTeacherId(@Param("semesterId") Long semesterId, @Param("teacherId") Long teacherId);

    // Tính số lớp giáo viên đã dạy trong 1 kì
    int countBySemesterIdAndTeacherId(Long semesterId, Long teacherId);
    //find ClassRooms by Department
    @Query("SELECT cr FROM ClassRoom cr WHERE cr.subject.department = :department")
    List<ClassRoom> findByDepartment(@Param("department") Department department);

    //search by departmentId directly
    @Query("SELECT cr FROM ClassRoom cr WHERE cr.subject.department.id = :departmentId")
    List<ClassRoom> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT cr FROM ClassRoom cr WHERE cr.subject.department.id = :departmentId AND cr.teacher.id = :teacherId")
    List<ClassRoom> findByDepartmentIdAndTeacherId(@Param("departmentId") Long departmentId, @Param("teacherId") Long teacherId);
}
