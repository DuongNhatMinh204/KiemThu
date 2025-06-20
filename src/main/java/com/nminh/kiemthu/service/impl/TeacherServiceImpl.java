package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.constants.Constant;
import com.nminh.kiemthu.entity.*;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.TeacherDTO;
import com.nminh.kiemthu.model.response.InfoTeacherResponseDTO;
import com.nminh.kiemthu.repository.*;
import com.nminh.kiemthu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private TuitionRepository tuitionRepository;

    @Override
    public Teacher createTeacherAccount(TeacherDTO teacherDTO) {
        Department department = departmentRepository.findById(teacherDTO.getDepartmentId())
                .orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        Degree degree = degreeRepository.findById(teacherDTO.getDegreeId())
                .orElseThrow(()-> new AppException(ErrorCode.NOT_EXISTS_DEGREE)) ;
        boolean exist = teacherRepository.existsByPhone(teacherDTO.getPhone()) ;
        if(exist) {
            throw new  AppException(ErrorCode.PHONE_EXISTS) ;
        }
        Teacher teacher = new Teacher();

        teacher.setFullName(teacherDTO.getFullName());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhone(teacherDTO.getPhone());
        teacher.setBirthday(teacherDTO.getBirthday());
        teacher.setDegree(degree);
        teacher.setDepartment(department);
        teacherRepository.save(teacher) ;
        // khi tạo giáo viên thì tạo lun tài khoản giáo viên với pass = 123456
        User user = new User();
        user.setUsername(teacherDTO.getPhone());
        user.setPassword("123456");
        user.setRoleName("teacher");
        user.setTeacherId(teacher.getId());

        userRepository.save(user);

        return teacher;

    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public List<Teacher> getAllTeachersOfDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        return teacherRepository.findByDepartment(department);

    }

    @Override
    public String deleteTeacherAccount(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.TEACHER_NOT_FOUND));
        teacherRepository.delete(teacher);
        return "Teacher deleted";
    }

    private Long getAmountPerLesson(Long semesterId) {
        return tuitionRepository.findBySemesterId(semesterId)
                .map(Tuition::getMoney)
                .orElseThrow(() -> new AppException(ErrorCode.TUITION_NOT_FOUND));
    }

    @Override
    public InfoTeacherResponseDTO getInfoTeacher(Long semesterId, Long departmentId, Long teacherId) {

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(()->new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()->new AppException(ErrorCode.TEACHER_NOT_FOUND));

        int soLopGiangDay = classRoomRepository.countBySemesterIdAndTeacherId(semesterId, teacherId);
        int soTietGiangDay = 0 ;
        List<ClassRoom> listCacLopGiangDay = classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId);
        double soTietQuyDoi = 0 ;
        for(ClassRoom classRoom : listCacLopGiangDay) {
            soTietQuyDoi += classRoom.getSubject().getNumberOfLessons() *
                    (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient());
        }

        Double heSoGiaoVien = teacher.getDegree().getDegreeCoefficient();
        Long amountPerLesson = getAmountPerLesson(semesterId);

        Double total_money = soTietQuyDoi*heSoGiaoVien*amountPerLesson ;

        InfoTeacherResponseDTO infoTeacherResponseDTO = new InfoTeacherResponseDTO();

        infoTeacherResponseDTO.setFullName(teacher.getFullName());
        infoTeacherResponseDTO.setEmail(teacher.getEmail());
        infoTeacherResponseDTO.setPhoneNumber(teacher.getPhone());
        infoTeacherResponseDTO.setDegree(teacher.getDegree());
        infoTeacherResponseDTO.setNumberOfLessons(soTietGiangDay);
        infoTeacherResponseDTO.setNumberOfClass(soLopGiangDay);
        infoTeacherResponseDTO.setTotal_money(total_money);

        return infoTeacherResponseDTO;
    }

    @Override
    public Teacher updateTeacherAccount(Long id, TeacherDTO teacherDTO) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.TEACHER_NOT_FOUND));
        teacher.setFullName(teacherDTO.getFullName());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhone(teacherDTO.getPhone());
        teacher.setBirthday(teacherDTO.getBirthday());
        teacher.setDepartment(departmentRepository.findById(teacherDTO.getDepartmentId()).orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND)));
        teacher.setDegree(degreeRepository.findById(teacherDTO.getDegreeId()).orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND)));

        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }
}
