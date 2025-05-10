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

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<TeacherSalaryResponse>> getTeacherSalariesBySemester(
            @PathVariable Long semesterId) {
        List<TeacherSalaryResponse> teacherSalaries = teacherSalaryService.getTeacherSalariesBySemester(semesterId);
        return ResponseEntity.ok(teacherSalaries);
    }

    @PutMapping("/{teacherSalaryId}/payment-status")
    public ResponseEntity<TeacherSalaryResponse> updatePaymentStatus(
            @PathVariable Long teacherSalaryId,
            @RequestParam boolean isPaid) {
        TeacherSalaryResponse updatedSalary = teacherSalaryService.updatePaymentStatus(teacherSalaryId, isPaid);
        return ResponseEntity.ok(updatedSalary);
    }
}