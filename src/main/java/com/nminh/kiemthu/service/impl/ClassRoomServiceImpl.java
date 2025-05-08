package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.*;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;
import com.nminh.kiemthu.repository.*;
import com.nminh.kiemthu.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassRoomServiceImpl implements ClassroomService {

    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public ClassRoom createClassroom(ClassRoomCreateDTO classRoomCreateDTO) {
        Semester semester = semesterRepository.findById(classRoomCreateDTO.getSemesterId())
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        Subject subject = subjectRepository.findById(classRoomCreateDTO.getSubjectId())
                .orElseThrow(()-> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        Teacher teacher = teacherRepository.findById(classRoomCreateDTO.getTeacherId())
                .orElseThrow(()->new AppException(ErrorCode.TEACHER_NOT_FOUND));

        ClassRoom classRoom = new ClassRoom(classRoomCreateDTO.getClassName(),classRoomCreateDTO.getNumberOfStudents(), semester, subject, teacher);

        return classRoomRepository.save(classRoom);
    }

    @Override
    public List<ClassRoom> getListClassrooms(Long departmentId, Long semesterId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()-> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(()-> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        List<ClassRoom> res = classRoomRepository.findByDepartmentAndSemester(departmentId,semesterId) ;
        if(res.isEmpty()){
            throw new AppException(ErrorCode.NOT_EXISTS_SUBJECT_IN_THIS_SEMESTER_OF_DEPARTMENT);
        }
        return res;
    }
}
