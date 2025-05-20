package com.nminh.kiemthu.controller.ketoan;

import com.nminh.kiemthu.model.response.TeacherSalaryResponse;
import com.nminh.kiemthu.service.TeacherSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher-salary")
public class TeacherSalaryController {

    @Autowired
    private TeacherSalaryService teacherSalaryService;

    @PostMapping("/calculate")
    public ResponseEntity<TeacherSalaryResponse> calculateTeacherSalary(
            @RequestParam Long teacherId,
            @RequestParam Long semesterId) {
        TeacherSalaryResponse teacherSalary = teacherSalaryService.calculateTeacherSalary(teacherId, semesterId);
        return ResponseEntity.ok(teacherSalary);
    }

    @GetMapping("/getBySemester")
    public ResponseEntity<List<TeacherSalaryResponse>> getTeacherSalariesAllBySemester(
            @RequestParam Long semesterId) {
        List<TeacherSalaryResponse> teacherSalaries = teacherSalaryService.getTeacherSalariesBySemester(semesterId);
        return ResponseEntity.ok(teacherSalaries);
    }
    @GetMapping("/getBySemesterAndTeacher")
    public ResponseEntity<TeacherSalaryResponse> getTeacherSalariesBySemester(
            @RequestParam Long semesterId,
            @RequestParam Long teacherId) {
        TeacherSalaryResponse teacherSalarie = teacherSalaryService.getTeacherSalary(teacherId, semesterId);
        return ResponseEntity.ok(teacherSalarie);
    }
    @GetMapping("/getByDepartment")
    public ResponseEntity<List<TeacherSalaryResponse>> getTeacherSalariesByDepartment(@RequestParam Long departmentId) {
        List<TeacherSalaryResponse> teacherSalarie = teacherSalaryService.getTeacherAllSalariesByDepartment(departmentId);
        return ResponseEntity.ok(teacherSalarie);
    }
    @PutMapping("/{teacherSalaryId}/payment-status")
    public ResponseEntity<TeacherSalaryResponse> updatePaymentStatus(
            @PathVariable Long teacherSalaryId,
            @RequestParam boolean isPaid) {
        TeacherSalaryResponse updatedSalary = teacherSalaryService.updatePaymentStatus(teacherSalaryId, isPaid);
        return ResponseEntity.ok(updatedSalary);
    }
    @PostMapping("/caculateBySemester/{semesterId}")
    public ResponseEntity<List<TeacherSalaryResponse>> calculateTeacherAllSalaryBySemester(@PathVariable Long semesterId){
        List<TeacherSalaryResponse> responses = teacherSalaryService.calculateTeacherAllSalaryBySemester(semesterId);
        return ResponseEntity.ok(responses);
    }
    @PostMapping("/caculateByDepartment/{departmentId}")
    public ResponseEntity<List<TeacherSalaryResponse>> calculateTeacherAllSalaryByDepartment(@PathVariable Long departmentId){
        List<TeacherSalaryResponse> responses = teacherSalaryService.calculateTeacherAllSalaryByDepartment(departmentId);
        return ResponseEntity.ok(responses);
    }
}