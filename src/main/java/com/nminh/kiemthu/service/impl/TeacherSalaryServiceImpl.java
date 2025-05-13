package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.constants.Constant;
import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.entity.TeacherSalary;
import com.nminh.kiemthu.enums.Degree;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.response.TeacherResponse;
import com.nminh.kiemthu.model.response.TeacherSalaryResponse;
import com.nminh.kiemthu.repository.ClassRoomRepository;
import com.nminh.kiemthu.repository.SemesterRepository;
import com.nminh.kiemthu.repository.TeacherRepository;
import com.nminh.kiemthu.repository.TeacherSalaryRepository;
import com.nminh.kiemthu.service.TeacherSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherSalaryServiceImpl implements TeacherSalaryService {

    @Autowired
    private TeacherSalaryRepository teacherSalaryRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    private TeacherResponse mapToTeacherResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();
        response.setId(teacher.getId());
        response.setName(teacher.getFullName());
        response.setEmail(teacher.getEmail());
        response.setDegree(teacher.getDegree());
        return response;
    }

    private TeacherSalaryResponse mapToTeacherSalaryResponse(TeacherSalary teacherSalary, List<ClassRoom> classRooms) {
        TeacherSalaryResponse response = new TeacherSalaryResponse();
        response.setTeacherRespose(mapToTeacherResponse(teacherSalary.getTeacher()));
//        response.setClassRoom(classRooms.forEach(classRoom -> classRoom.getClassName()));
        response.setTotalHoursTeaching(teacherSalary.getTotalHoursTeaching());
        response.setTotalSalary(teacherSalary.getTotalSalary());
        response.setStatusPayment(teacherSalary.getStatusPayment());
        return response;
    }

    @Override
    public TeacherSalaryResponse calculateTeacherSalary(Long teacherId, Long semesterId) {
        // Fetch teacher and semester
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        // Fetch all class rooms for the teacher in the given semester
        List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId);
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // Calculate total converted hours and actual hours
        double totalActualHours = 0.0;
        double totalConvertedHours = 0.0;
        for (ClassRoom classRoom : classRooms) {
            double actualHours = classRoom.getSubject().getCredits() ;
            double subjectCoefficient = classRoom.getSubject().getModule_coefficient();
            double classCoefficient = classRoom.getClassCoefficient();
            totalActualHours += actualHours;
            totalConvertedHours += actualHours * (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient()) ;
        }

        // Determine lecturer coefficient based on degree
        double lecturerCoefficient;
        if (teacher.getDegree().equals(Degree.THAC_SI)) {
            lecturerCoefficient = Constant.HE_SO_THAC_SI;
        } else if (teacher.getDegree().equals(Degree.TIEN_SI)) {
            lecturerCoefficient = Constant.HE_SO_TIEN_SI;
        } else if (teacher.getDegree().equals(Degree.PHO_GIAO_SU)) {
            lecturerCoefficient = Constant.HE_SO_PHO_GIAO_SU;
        } else if (teacher.getDegree().equals(Degree.GIAO_SU)) {
            lecturerCoefficient = Constant.HE_SO_GIAO_SU;
        } else {
            lecturerCoefficient = 1.0;
        }

        // Calculate total salary
        double totalSalary = totalConvertedHours * lecturerCoefficient * Constant.TIEN_DAY_MOT_TIET;

        // Create or update TeacherSalary record
        TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                .orElse(new TeacherSalary());
        teacherSalary.setTeacher(teacher);
        teacherSalary.setSemester(semester);
        teacherSalary.setTotalHoursTeaching(totalConvertedHours);
        teacherSalary.setTotalSalary(totalSalary);
        teacherSalary.setStatusPayment(com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);

        TeacherSalary savedSalary = teacherSalaryRepository.save(teacherSalary);
        return mapToTeacherSalaryResponse(savedSalary, classRooms);
    }

    @Override
    public List<TeacherSalaryResponse> getTeacherSalariesBySemester(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        List<TeacherSalary> teacherSalaries = teacherSalaryRepository.findBySemester(semester);
        return teacherSalaries.stream()
                .map(salary -> {
                    List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(
                            semesterId, salary.getTeacher().getId());
                    return mapToTeacherSalaryResponse(salary, classRooms);
                })
                .collect(Collectors.toList());
    }

    @Override
    public TeacherSalaryResponse updatePaymentStatus(Long teacherSalaryId, boolean isPaid) {
        TeacherSalary teacherSalary = teacherSalaryRepository.findById(teacherSalaryId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_SALARY_NOT_FOUND));
        teacherSalary.setStatusPayment(isPaid ? com.nminh.kiemthu.enums.StatusPayment.DA_THANH_TOAN : com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);
        TeacherSalary updatedSalary = teacherSalaryRepository.save(teacherSalary);
        List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(
                updatedSalary.getSemester().getId(), updatedSalary.getTeacher().getId());
        return mapToTeacherSalaryResponse(updatedSalary, classRooms);
    }
}