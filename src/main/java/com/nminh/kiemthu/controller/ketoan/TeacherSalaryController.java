package com.nminh.kiemthu.controller.ketoan;

import com.nminh.kiemthu.model.response.ReportResponse;
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
        TeacherSalaryResponse response = teacherSalaryService.calculateTeacherSalary(teacherId, semesterId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-semester")
    public ResponseEntity<List<TeacherSalaryResponse>> getTeacherSalariesBySemester(
            @RequestParam Long semesterId) {
        List<TeacherSalaryResponse> responses = teacherSalaryService.getTeacherSalariesBySemester(semesterId);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/by-schoolYear")
    public ResponseEntity<List<TeacherSalaryResponse>> getTeacherSalaryBySchoolYear(@RequestParam String year){
        List<TeacherSalaryResponse> responses = teacherSalaryService.getTeacherAllSalariesBySchoolYear(year);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/by-semester-and-teacher")
    public ResponseEntity<TeacherSalaryResponse> getTeacherSalary(
            @RequestParam Long semesterId,
            @RequestParam Long teacherId) {
        TeacherSalaryResponse response = teacherSalaryService.getTeacherSalary(teacherId, semesterId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-department")
    public ResponseEntity<List<TeacherSalaryResponse>> getTeacherAllSalariesByDepartment(
            @RequestParam Long departmentId) {
        List<TeacherSalaryResponse> responses = teacherSalaryService.getTeacherAllSalariesByDepartment(departmentId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{teacherSalaryId}/payment-status")
    public ResponseEntity<TeacherSalaryResponse> updatePaymentStatus(
            @PathVariable Long teacherSalaryId,
            @RequestParam boolean isPaid) {
        TeacherSalaryResponse response = teacherSalaryService.updatePaymentStatus(teacherSalaryId, isPaid);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-by-semester/{semesterId}")
    public ResponseEntity<List<TeacherSalaryResponse>> calculateTeacherAllSalaryBySemester(
            @PathVariable Long semesterId) {
        List<TeacherSalaryResponse> responses = teacherSalaryService.calculateTeacherAllSalaryBySemester(semesterId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/calculate-by-department/{departmentId}")
    public ResponseEntity<List<TeacherSalaryResponse>> calculateTeacherAllSalaryByDepartment(
            @PathVariable Long departmentId) {
        List<TeacherSalaryResponse> responses = teacherSalaryService.calculateTeacherAllSalaryByDepartment(departmentId);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/export-report")
    public ResponseEntity<ReportResponse> exportReport(@RequestParam Long teacherSalaryId){
        return ResponseEntity.ok(teacherSalaryService.exportReport(teacherSalaryId));
    }
}