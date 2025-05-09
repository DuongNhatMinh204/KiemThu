package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.constants.Constant;
import com.nminh.kiemthu.entity.*;
import com.nminh.kiemthu.enums.Degree;
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

    @Override
    public Teacher createTeacherAccount(TeacherDTO teacherDTO) {
        Department department = departmentRepository.findById(teacherDTO.getDepartmentId())
                .orElseThrow(()->new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        boolean exist = teacherRepository.existsByPhone(teacherDTO.getPhone()) ;
        if(exist) {
            throw new  AppException(ErrorCode.PHONE_EXISTS) ;
        }
        Teacher teacher = new Teacher();

        teacher.setFullName(teacherDTO.getFullName());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhone(teacherDTO.getPhone());
        teacher.setBirthday(teacherDTO.getBirthday());
        teacher.setDegree(teacherDTO.getDegree());
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
            soTietGiangDay += classRoom.getSubject().getCredits() ; // bao nhiêu tín là bấy nhiêu tieets
            // so tiet quy doi = so tiet thuc te * ( he so hp + he so lop
            soTietQuyDoi += classRoom.getSubject().getCredits() * (classRoom.getSubject().getModule_coefficient() + classRoom.getClassCoefficient()) ;
        }

        Double heSoGiaoVien ;
        if(teacher.getDegree().equals(Degree.THAC_SI)){
            heSoGiaoVien = Constant.HE_SO_THAC_SI ;
        }else if (teacher.getDegree().equals(Degree.TIEN_SI)){
            heSoGiaoVien = Constant.HE_SO_TIEN_SI ;
        } else if (teacher.getDegree().equals(Degree.PHO_GIAO_SU)) {
            heSoGiaoVien = Constant.HE_SO_PHO_GIAO_SU ;
        } else if (teacher.getDegree().equals(Degree.GIAO_SU)) {
            heSoGiaoVien = Constant.HE_SO_GIAO_SU ;
        }else {
            heSoGiaoVien = 1.0 ;
        }

        Double total_money = soTietQuyDoi*heSoGiaoVien*Constant.TIEN_DAY_MOT_TIET ;

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
}
