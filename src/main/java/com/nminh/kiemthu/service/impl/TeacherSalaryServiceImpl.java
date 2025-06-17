package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.entity.TeacherSalary;
import com.nminh.kiemthu.entity.Tuition;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.enums.StatusPayment;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.response.ClassRoomResponse;
import com.nminh.kiemthu.model.response.ReportResponse;
import com.nminh.kiemthu.model.response.TeacherResponse;
import com.nminh.kiemthu.model.response.TeacherSalaryResponse;
import com.nminh.kiemthu.repository.ClassRoomRepository;
import com.nminh.kiemthu.repository.SemesterRepository;
import com.nminh.kiemthu.repository.TeacherRepository;
import com.nminh.kiemthu.repository.TeacherSalaryRepository;
import com.nminh.kiemthu.repository.TuitionRepository;
import com.nminh.kiemthu.service.TeacherSalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    private TuitionRepository tuitionRepository;

    private TeacherResponse mapToTeacherResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();
        response.setId(teacher.getId());
        response.setName(teacher.getFullName());
        response.setEmail(teacher.getEmail());
        response.setDegree(teacher.getDegree());
        response.setDepartment(teacher.getDepartment().getFullName());
        return response;
    }

    private TeacherSalaryResponse mapToTeacherSalaryResponse(TeacherSalary teacherSalary, List<ClassRoom> classRooms) {
        TeacherSalaryResponse response = new TeacherSalaryResponse();
        response.setTeacherResponse(mapToTeacherResponse(teacherSalary.getTeacher()));
        response.setClassRoom(classRooms.stream()
                .map(ClassRoom::getClassName)
                .collect(Collectors.toList()));
        response.setTotalHoursTeaching(roundToTwoDecimalPlaces(teacherSalary.getTotalHoursTeaching()));
        response.setTotalSalary(roundToTwoDecimalPlaces(teacherSalary.getTotalSalary()));
        response.setStatusPayment(teacherSalary.getStatusPayment());
        response.setId(teacherSalary.getId());
        return response;
    }

    private Double roundToTwoDecimalPlaces(Double value) {
        if (value == null) return 0.0;
        BigDecimal bd = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private Long getAmountPerLesson(Long semesterId) {
        return tuitionRepository.findBySemesterId(semesterId)
                .map(Tuition::getMoney)
                .orElseThrow(() -> new AppException(ErrorCode.TUITION_NOT_FOUND));
    }

    @Override
    public TeacherSalaryResponse calculateTeacherSalary(Long teacherId, Long semesterId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId)
                .stream()
                .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                .collect(Collectors.toList());
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        int soLopGiangDay = classRoomRepository.countBySemesterIdAndTeacherId(semesterId, teacherId);
        double soTietQuyDoi = 0;
        for (ClassRoom classRoom : classRooms) {
            soTietQuyDoi += classRoom.getSubject().getNumberOfLessons() *
                    (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient());
        }
        Double heSoGiaoVien = teacher.getDegree().getDegreeCoefficient();
        Long amountPerLesson = getAmountPerLesson(semesterId);
        Double totalMoney = soTietQuyDoi * heSoGiaoVien * amountPerLesson;

        // Kiểm tra bản ghi hiện có để giữ nguyên trạng thái thanh toán
        TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                .orElseGet(() -> {
                    TeacherSalary newSalary = new TeacherSalary();
                    newSalary.setTeacher(teacher);
                    newSalary.setSemester(semester);
                    // Kiểm tra trạng thái từ các bản ghi khác nếu có
                    Optional<StatusPayment> existingStatus = teacherSalaryRepository.findByTeacherId(teacherId)
                            .stream()
                            .filter(s -> s.getStatusPayment() != null)
                            .map(TeacherSalary::getStatusPayment)
                            .findFirst();
                    newSalary.setStatusPayment(existingStatus.orElse(null)); // Giữ trạng thái hiện có nếu có
                    return newSalary;
                });
        teacherSalary.setTeacher(teacher);
        teacherSalary.setSemester(semester);
        teacherSalary.setTotalHoursTeaching(soTietQuyDoi);
        teacherSalary.setTotalSalary(totalMoney);
        // Không cập nhật trạng thái thanh toán

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
                                    semesterId, salary.getTeacher().getId())
                            .stream()
                            .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                            .collect(Collectors.toList());
                    return mapToTeacherSalaryResponse(salary, classRooms);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSalaryResponse> calculateTeacherAllSalaryBySemester(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        List<ClassRoom> classRooms = classRoomRepository.findBySemesterId(semesterId)
                .stream()
                .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                .collect(Collectors.toList());
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        Set<Long> teacherIds = classRooms.stream()
                .map(classRoom -> classRoom.getTeacher().getId())
                .collect(Collectors.toSet());

        return teacherIds.stream().map(teacherId -> {
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
            List<ClassRoom> teacherClassRooms = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId)
                    .stream()
                    .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                    .collect(Collectors.toList());

            double soTietQuyDoi = 0;
            for (ClassRoom classRoom : teacherClassRooms) {
                soTietQuyDoi += classRoom.getSubject().getNumberOfLessons() *
                        (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient());
            }
            Double heSoGiaoVien = teacher.getDegree().getDegreeCoefficient();
            Long amountPerLesson = getAmountPerLesson(semesterId);
            Double totalMoney = soTietQuyDoi * heSoGiaoVien * amountPerLesson;

            TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                    .orElseGet(() -> {
                        TeacherSalary newSalary = new TeacherSalary();
                        newSalary.setTeacher(teacher);
                        newSalary.setSemester(semester);
                        // Kiểm tra trạng thái từ các bản ghi khác nếu có
                        Optional<StatusPayment> existingStatus = teacherSalaryRepository.findByTeacherId(teacherId)
                                .stream()
                                .filter(s -> s.getStatusPayment() != null)
                                .map(TeacherSalary::getStatusPayment)
                                .findFirst();
                        newSalary.setStatusPayment(existingStatus.orElse(null)); // Giữ trạng thái hiện có nếu có
                        return newSalary;
                    });
            teacherSalary.setTeacher(teacher);
            teacherSalary.setSemester(semester);
            teacherSalary.setTotalHoursTeaching(soTietQuyDoi);
            teacherSalary.setTotalSalary(totalMoney);
            // Không cập nhật trạng thái thanh toán

            TeacherSalary savedSalary = teacherSalaryRepository.save(teacherSalary);
            return mapToTeacherSalaryResponse(savedSalary, teacherClassRooms);
        }).collect(Collectors.toList());
    }

    @Override
    public TeacherSalaryResponse updatePaymentStatus(Long teacherSalaryId, boolean isPaid) {
        TeacherSalary teacherSalary = teacherSalaryRepository.findById(teacherSalaryId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_SALARY_NOT_FOUND));

        // Không cho phép sửa từ "Đã thanh toán" thành "Chưa thanh toán"
        if (teacherSalary.getStatusPayment() == StatusPayment.DA_THANH_TOAN && !isPaid) {
            throw new AppException(ErrorCode.PAYMENT_STATUS_UPDATE_NOT_ALLOWED);
        }

        // Cập nhật trạng thái thành DA_THANH_TOAN nếu isPaid là true
        teacherSalary.setStatusPayment(isPaid ? StatusPayment.DA_THANH_TOAN : StatusPayment.CHUA_THANH_TOAN);
        TeacherSalary updatedSalary = teacherSalaryRepository.save(teacherSalary);
        List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(
                        updatedSalary.getSemester().getId(), updatedSalary.getTeacher().getId())
                .stream()
                .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                .collect(Collectors.toList());
        return mapToTeacherSalaryResponse(updatedSalary, classRooms);
    }

    @Override
    public List<TeacherSalaryResponse> getTeacherAllSalariesBySchoolYear(String year) {
        List<Semester> semesters = semesterRepository.findBySchoolYear(year);
        if (semesters.isEmpty()) {
            throw new AppException(ErrorCode.SEMESTER_NOT_FOUND);
        }

        List<TeacherSalaryResponse> teacherSalaryResponses = new ArrayList<>();
        Set<Long> teacherIds = new HashSet<>();

        // Thu thập tất cả teacherIds từ các lớp trong các học kỳ
        for (Semester semester : semesters) {
            List<ClassRoom> classRooms = classRoomRepository.findBySemesterId(semester.getId())
                    .stream()
                    .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                    .collect(Collectors.toList());
            teacherIds.addAll(classRooms.stream()
                    .map(classRoom -> classRoom.getTeacher().getId())
                    .collect(Collectors.toSet()));
        }

        // Tính lương cho từng giáo viên
        for (Long teacherId : teacherIds) {
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
            double totalHoursTeaching = 0.0;
            double totalSalary = 0.0;
            List<ClassRoom> allClassRooms = new ArrayList<>();

            for (Semester semester : semesters) {
                List<ClassRoom> teacherClassRooms = classRoomRepository.findBySemesterIdAndTeacherId(semester.getId(), teacherId)
                        .stream()
                        .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                        .collect(Collectors.toList());

                if (!teacherClassRooms.isEmpty()) {
                    allClassRooms.addAll(teacherClassRooms);
                    double semesterHours = 0.0;
                    for (ClassRoom classRoom : teacherClassRooms) {
                        semesterHours += classRoom.getSubject().getNumberOfLessons() *
                                (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient());
                    }
                    Long amountPerLesson = getAmountPerLesson(semester.getId());
                    Double heSoGiaoVien = teacher.getDegree().getDegreeCoefficient();
                    totalHoursTeaching += semesterHours;
                    totalSalary += semesterHours * heSoGiaoVien * amountPerLesson;
                }
            }

            if (!allClassRooms.isEmpty()) {
                // Sử dụng học kỳ đầu tiên làm đại diện để lưu
                TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesters.get(0).getId())
                        .orElseGet(() -> {
                            TeacherSalary newSalary = new TeacherSalary();
                            newSalary.setTeacher(teacher);
                            newSalary.setSemester(semesters.get(0));
                            // Kiểm tra trạng thái từ các bản ghi khác nếu có
                            Optional<StatusPayment> existingStatus = teacherSalaryRepository.findByTeacherId(teacherId)
                                    .stream()
                                    .filter(s -> s.getStatusPayment() != null)
                                    .map(TeacherSalary::getStatusPayment)
                                    .findFirst();
                            newSalary.setStatusPayment(existingStatus.orElse(null)); // Giữ trạng thái hiện có nếu có
                            return newSalary;
                        });
                teacherSalary.setTeacher(teacher);
                teacherSalary.setSemester(semesters.get(0));
                teacherSalary.setTotalHoursTeaching(totalHoursTeaching);
                teacherSalary.setTotalSalary(totalSalary);
                // Không cập nhật trạng thái thanh toán

                TeacherSalary savedSalary = teacherSalaryRepository.save(teacherSalary);
                teacherSalaryResponses.add(mapToTeacherSalaryResponse(savedSalary, allClassRooms));
            }
        }

        return teacherSalaryResponses;
    }

    @Override
    public List<TeacherSalaryResponse> calculateTeacherAllSalaryByDepartment(Long departmentId) {
        // Lấy tất cả lớp trong khoa
        List<ClassRoom> classRooms = classRoomRepository.findByDepartmentId(departmentId)
                .stream()
                .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                .collect(Collectors.toList());
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // Lấy danh sách giảng viên duy nhất từ các lớp
        Set<Long> teacherIds = classRooms.stream()
                .map(classRoom -> classRoom.getTeacher().getId())
                .collect(Collectors.toSet());

        // Tính lương cho từng giảng viên
        return teacherIds.stream().map(teacherId -> {
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
            Long semesterId = classRooms.stream()
                    .filter(classRoom -> classRoom.getTeacher().getId().equals(teacherId))
                    .map(classRoom -> classRoom.getSemester().getId())
                    .findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
            Semester semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

            List<ClassRoom> teacherClassRooms = classRoomRepository.findByDepartmentIdAndTeacherId(departmentId, teacherId)
                    .stream()
                    .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                    .collect(Collectors.toList());

            double soTietQuyDoi = 0;
            for (ClassRoom classRoom : teacherClassRooms) {
                soTietQuyDoi += classRoom.getSubject().getNumberOfLessons() *
                        (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient());
            }
            Double heSoGiaoVien = teacher.getDegree().getDegreeCoefficient();
            Long amountPerLesson = getAmountPerLesson(semesterId);
            Double totalMoney = soTietQuyDoi * heSoGiaoVien * amountPerLesson;

            TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                    .orElseGet(() -> {
                        TeacherSalary newSalary = new TeacherSalary();
                        newSalary.setTeacher(teacher);
                        newSalary.setSemester(semester);
                        // Kiểm tra trạng thái từ các bản ghi khác nếu có
                        Optional<StatusPayment> existingStatus = teacherSalaryRepository.findByTeacherId(teacherId)
                                .stream()
                                .filter(s -> s.getStatusPayment() != null)
                                .map(TeacherSalary::getStatusPayment)
                                .findFirst();
                        newSalary.setStatusPayment(existingStatus.orElse(null)); // Giữ trạng thái hiện có nếu có
                        return newSalary;
                    });
            teacherSalary.setTeacher(teacher);
            teacherSalary.setSemester(semester);
            teacherSalary.setTotalHoursTeaching(soTietQuyDoi);
            teacherSalary.setTotalSalary(totalMoney);
            // Không cập nhật trạng thái thanh toán

            TeacherSalary savedSalary = teacherSalaryRepository.save(teacherSalary);
            return mapToTeacherSalaryResponse(savedSalary, teacherClassRooms);
        }).collect(Collectors.toList());
    }

    @Override
    public List<TeacherSalaryResponse> getTeacherAllSalariesByDepartment(Long departmentId) {
        // Lấy tất cả lớp trong khoa
        List<ClassRoom> classRooms = classRoomRepository.findByDepartmentId(departmentId)
                .stream()
                .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                .collect(Collectors.toList());
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // Lấy danh sách giảng viên duy nhất từ các lớp
        Set<Long> teacherIds = classRooms.stream()
                .map(classRoom -> classRoom.getTeacher().getId())
                .collect(Collectors.toSet());

        // Lấy thông tin lương của từng giảng viên
        return teacherIds.stream().map(teacherId -> {
            // Lấy semesterId từ lớp của giảng viên
            Long semesterId = classRooms.stream()
                    .filter(classRoom -> classRoom.getTeacher().getId().equals(teacherId))
                    .map(classRoom -> classRoom.getSemester().getId())
                    .findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

            // Lấy thông tin lương từ TeacherSalary
            TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_SALARY_NOT_FOUND));

            // Lấy danh sách lớp của giảng viên trong khoa
            List<ClassRoom> teacherClassRooms = classRoomRepository.findByDepartmentIdAndTeacherId(departmentId, teacherId)
                    .stream()
                    .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                    .collect(Collectors.toList());

            // Chuyển đổi thành TeacherSalaryResponse
            return mapToTeacherSalaryResponse(teacherSalary, teacherClassRooms);
        }).collect(Collectors.toList());
    }

    @Override
    public TeacherSalaryResponse getTeacherSalary(Long teacherId, Long semesterId) {
        // Kiểm tra giảng viên và học kỳ
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        // Lấy thông tin lương
        TeacherSalary teacherSalary = teacherSalaryRepository.findByTeacherIdAndSemesterId(teacherId, semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_SALARY_NOT_FOUND));

        // Lấy danh sách lớp của giảng viên trong học kỳ
        List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId)
                .stream()
                .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                .collect(Collectors.toList());
        if (classRooms.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // Chuyển đổi thành TeacherSalaryResponse và trả về
        return mapToTeacherSalaryResponse(teacherSalary, classRooms);
    }

    @Override
    public ReportResponse exportReport(Long teacherSalaryId){
        TeacherSalary teacherSalary = teacherSalaryRepository.findById(teacherSalaryId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_SALARY_NOT_FOUND));
        TeacherResponse teacherResponse = new TeacherResponse();
        Teacher teacher = teacherSalary.getTeacher();
        List<ClassRoom> classRooms = classRoomRepository.findBySemesterIdAndTeacherId(teacherSalary.getSemester().getId(), teacher.getId())
                .stream()
                .filter(classRoom -> classRoom.getTeacher() != null && classRoom.getTeacher().getId() != null)
                .toList();
        List<ClassRoomResponse> classRoomResponses = new ArrayList<>();
        for (ClassRoom classRoom : classRooms){
            ClassRoomResponse classRoomResponse = new ClassRoomResponse();

            classRoomResponse.setClassCoefficient(classRoom.getClassCoefficient());
            classRoomResponse.setClassName(classRoom.getClassName());
            classRoomResponse.setSubject(classRoom.getSubject().getSubjectName());
            classRoomResponse.setSemesterName(classRoom.getSemester().getSemesterName());
            classRoomResponse.setSchoolYear(classRoom.getSemester().getSchoolYear());
            classRoomResponse.setNumberOfStudents(classRoom.getNumberOfStudents());

            classRoomResponses.add(classRoomResponse);
        }
        teacherResponse.setDepartment(teacher.getDepartment().getFullName());
        teacherResponse.setName(teacher.getFullName());
        teacherResponse.setDegree(teacher.getDegree());
        teacherResponse.setEmail(teacher.getEmail());
        teacherResponse.setId(teacher.getId());


        ReportResponse response = new ReportResponse();

        response.setTeacherResponse(teacherResponse);
        response.setClassRoomResponses(classRoomResponses);
        response.setTotalSalary(teacherSalary.getTotalSalary());
        response.setStatusPayment(teacherSalary.getStatusPayment());
        response.setTotalHoursTeaching(teacherSalary.getTotalHoursTeaching());

        return response;
    }
}