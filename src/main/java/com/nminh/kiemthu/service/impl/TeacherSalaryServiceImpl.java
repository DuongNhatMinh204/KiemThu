package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.constants.Constant;
import com.nminh.kiemthu.entity.*;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.response.TeacherResponse;
import com.nminh.kiemthu.model.response.TeacherSalaryResponse;
import com.nminh.kiemthu.repository.*;
import com.nminh.kiemthu.service.TeacherSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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

    @Autowired
    private DepartmentRepository departmentRepository;

    private TeacherResponse mapToTeacherResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();
        response.setId(teacher.getId());
        response.setName(teacher.getFullName());
        response.setDepartment(teacher.getDepartment().getFullName());
        response.setEmail(teacher.getEmail());
        response.setDegree(teacher.getDegree());
        return response;
    }

    private TeacherSalaryResponse mapToTeacherSalaryResponse(TeacherSalary teacherSalary, List<ClassRoom> classRooms) {
        TeacherSalaryResponse response = new TeacherSalaryResponse();
        response.setTeacherRespose(mapToTeacherResponse(teacherSalary.getTeacher()));
        response.setClassRoom(classRooms.stream()
                .map(ClassRoom::getClassName)
                .collect(Collectors.toList()));
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

        int soLopGiangDay = classRoomRepository.countBySemesterIdAndTeacherId(semesterId, teacherId);
        int soTietGiangDay = 0;
        List<ClassRoom> listCacLopGiangDay = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId);
        double soTietQuyDoi = 0;
        for (ClassRoom classRoom : listCacLopGiangDay) {
            soTietGiangDay += classRoom.getSubject().getCredits(); // bao nhiêu tín là bấy nhiêu tiết
            // so tiet quy doi = so tiet thuc te * ( he so hp + he so lop
            soTietQuyDoi += classRoom.getSubject().getCredits() * (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient());
        }
        Double heSoGiaoVien = teacher.getDegree().getDegreeCoefficient();

        Double total_money = soTietQuyDoi * heSoGiaoVien * Constant.TIEN_DAY_MOT_TIET;

        // Create or update TeacherSalary record
        TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                .orElse(new TeacherSalary());

        teacherSalary.setTeacher(teacher);
        teacherSalary.setSemester(semester);
        teacherSalary.setTotalHoursTeaching(soTietQuyDoi);
        teacherSalary.setTotalSalary(total_money);
        teacherSalary.setStatusPayment(com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);

        TeacherSalary savedSalary = teacherSalaryRepository.save(teacherSalary);
        return mapToTeacherSalaryResponse(savedSalary, classRooms);
    }


    @Override
    public List<TeacherSalaryResponse> calculateTeacherAllSalaryBySemester(Long semesterId) {
        // Kiểm tra học kỳ
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        // Lấy tất cả lớp trong học kỳ
        List<ClassRoom> classRooms = classRoomRepository.findBySemesterId(semesterId);
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // Lấy danh sách giảng viên duy nhất từ các lớp
        Set<Long> teacherIds = classRooms.stream()
                .map(classRoom -> classRoom.getTeacher().getId())
                .collect(Collectors.toSet());

        // Tính lương cho từng giảng viên
        return teacherIds.stream().map(teacherId -> {
            // Lấy thông tin giảng viên
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

            // Lấy các lớp mà giảng viên dạy
            List<ClassRoom> teacherClassRooms = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId);

            // Tính toán số tiết và lương
            double soTietQuyDoi = 0;
            for (ClassRoom classRoom : teacherClassRooms) {
                soTietQuyDoi += classRoom.getSubject().getCredits() *
                        (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient());
            }
            Double heSoGiaoVien = teacher.getDegree().getDegreeCoefficient();
            Double totalMoney = soTietQuyDoi * heSoGiaoVien * Constant.TIEN_DAY_MOT_TIET;

            // Lưu hoặc cập nhật TeacherSalary
            TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                    .orElse(new TeacherSalary());
            teacherSalary.setTeacher(teacher);
            teacherSalary.setSemester(semester);
            teacherSalary.setTotalHoursTeaching(soTietQuyDoi);
            teacherSalary.setTotalSalary(totalMoney);
            teacherSalary.setStatusPayment(com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);

            TeacherSalary savedSalary = teacherSalaryRepository.save(teacherSalary);
            return mapToTeacherSalaryResponse(savedSalary, teacherClassRooms);
        }).collect(Collectors.toList());
    }

    @Override
    public List<TeacherSalaryResponse> calculateTeacherAllSalaryByDepartment(Long departmentId) {
        // Kiểm tra department
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        // Lấy tất cả lớp trong department
        List<ClassRoom> classRooms = classRoomRepository.findByDepartmentId(departmentId);
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // Lấy danh sách giảng viên duy nhất từ các lớp trong department
        Set<Long> teacherIds = classRooms.stream()
                .map(classRoom -> classRoom.getTeacher().getId())
                .collect(Collectors.toSet());

        // Tính lương cho từng giảng viên trong department
        return teacherIds.stream().map(teacherId -> {
            // Lấy thông tin giảng viên
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

            // Lấy các lớp mà giảng viên dạy trong department
            List<ClassRoom> teacherClassRooms = classRoomRepository.findByDepartmentIdAndTeacherId(departmentId, teacherId);

            // Tính toán số tiết quy đổi và lương
            double soTietQuyDoi = 0;
            for (ClassRoom classRoom : teacherClassRooms) {
                soTietQuyDoi += classRoom.getSubject().getCredits() *
                        (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient());
            }
            Double heSoGiaoVien = teacher.getDegree().getDegreeCoefficient();
            Double totalMoney = soTietQuyDoi * heSoGiaoVien * Constant.TIEN_DAY_MOT_TIET;

            // Lưu hoặc cập nhật TeacherSalary (dựa trên semester đầu tiên của lớp, có thể cần điều chỉnh)
            ClassRoom firstClassRoom = teacherClassRooms.get(0); // Lấy semester từ lớp đầu tiên
            Semester semester = firstClassRoom.getSemester();
            TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semester.getId())
                    .orElse(new TeacherSalary());
            teacherSalary.setTeacher(teacher);
            teacherSalary.setSemester(semester);
            teacherSalary.setTotalHoursTeaching(soTietQuyDoi);
            teacherSalary.setTotalSalary(totalMoney);
            teacherSalary.setStatusPayment(com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);

            TeacherSalary savedSalary = teacherSalaryRepository.save(teacherSalary);
            return mapToTeacherSalaryResponse(savedSalary, teacherClassRooms);
        }).collect(Collectors.toList());
    }
    @Override
    public TeacherSalaryResponse getTeacherSalary(Long teacherId, Long semesterId) {
        // Fetch teacher and semester
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        // Fetch existing TeacherSalary record
        TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_SALARY_NOT_FOUND));

        // Maintain existing StatusPayment or set to CHUA_THANH_TOAN if not DA_THANH_TOAN
        if (!teacherSalary.getStatusPayment().equals(com.nminh.kiemthu.enums.StatusPayment.DA_THANH_TOAN)) {
            teacherSalary.setStatusPayment(com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);
            teacherSalary = teacherSalaryRepository.save(teacherSalary);
        }

        // Fetch related class rooms
        List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId);
        return mapToTeacherSalaryResponse(teacherSalary, classRooms);
    }

    @Override
    public List<TeacherSalaryResponse> getTeacherSalariesBySemester(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        List<TeacherSalary> teacherSalaries = teacherSalaryRepository.findBySemester(semester);
        return teacherSalaries.stream()
                .map(salary -> {
                    // Maintain existing StatusPayment or set to CHUA_THANH_TOAN if not DA_THANH_TOAN
                    if (!salary.getStatusPayment().equals(com.nminh.kiemthu.enums.StatusPayment.DA_THANH_TOAN)) {
                        salary.setStatusPayment(com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);
                        salary = teacherSalaryRepository.save(salary);
                    }
                    List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(
                            semesterId, salary.getTeacher().getId());
                    return mapToTeacherSalaryResponse(salary, classRooms);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSalaryResponse> getTeacherAllSalariesBySemester(Long semesterId) {
        // Kiểm tra học kỳ
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        // Lấy tất cả lớp trong học kỳ
        List<ClassRoom> classRooms = classRoomRepository.findBySemesterId(semesterId);
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // Lấy danh sách giảng viên duy nhất từ các lớp
        Set<Long> teacherIds = classRooms.stream()
                .map(classRoom -> classRoom.getTeacher().getId())
                .collect(Collectors.toSet());

        // Lấy thông tin lương cho từng giảng viên
        return teacherIds.stream().map(teacherId -> {
            // Lấy thông tin giảng viên
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

            // Lấy các lớp mà giảng viên dạy
            List<ClassRoom> teacherClassRooms = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId);

            // Lấy hoặc tạo TeacherSalary record
            TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_SALARY_NOT_FOUND));

            // Maintain existing StatusPayment or set to CHUA_THANH_TOAN if not DA_THANH_TOAN
            if (!teacherSalary.getStatusPayment().equals(com.nminh.kiemthu.enums.StatusPayment.DA_THANH_TOAN)) {
                teacherSalary.setStatusPayment(com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);
                teacherSalary = teacherSalaryRepository.save(teacherSalary);
            }

            return mapToTeacherSalaryResponse(teacherSalary, teacherClassRooms);
        }).collect(Collectors.toList());
    }

    @Override
    public List<TeacherSalaryResponse> getTeacherAllSalariesByDepartment(Long departmentId) {
        // Kiểm tra department
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        // Lấy tất cả lớp trong department
        List<ClassRoom> classRooms = classRoomRepository.findByDepartmentId(departmentId);
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // Lấy danh sách giảng viên duy nhất từ các lớp trong department
        Set<Long> teacherIds = classRooms.stream()
                .map(classRoom -> classRoom.getTeacher().getId())
                .collect(Collectors.toSet());

        // Lấy thông tin lương cho từng giảng viên trong department
        return teacherIds.stream().map(teacherId -> {
            // Lấy thông tin giảng viên
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

            // Lấy các lớp mà giảng viên dạy trong department
            List<ClassRoom> teacherClassRooms = classRoomRepository.findByDepartmentIdAndTeacherId(departmentId, teacherId);

            // Lấy hoặc tạo TeacherSalary record (dựa trên semester đầu tiên của lớp)
            ClassRoom firstClassRoom = teacherClassRooms.stream().findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
            Semester semester = firstClassRoom.getSemester();
            TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semester.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_SALARY_NOT_FOUND));

            // Maintain existing StatusPayment or set to CHUA_THANH_TOAN if not DA_THANH_TOAN
            if (!teacherSalary.getStatusPayment().equals(com.nminh.kiemthu.enums.StatusPayment.DA_THANH_TOAN)) {
                teacherSalary.setStatusPayment(com.nminh.kiemthu.enums.StatusPayment.CHUA_THANH_TOAN);
                teacherSalary = teacherSalaryRepository.save(teacherSalary);
            }

            return mapToTeacherSalaryResponse(teacherSalary, teacherClassRooms);
        }).collect(Collectors.toList());
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