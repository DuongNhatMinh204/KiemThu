package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.ClassRoom;
import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.entity.Subject;
import com.nminh.kiemthu.entity.Teacher;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;
import com.nminh.kiemthu.repository.ClassRoomRepository;
import com.nminh.kiemthu.repository.SemesterRepository;
import com.nminh.kiemthu.repository.SubjectRepository;
import com.nminh.kiemthu.repository.TeacherRepository;
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
    public List<ClassRoom> findClassRoomsBySemesterId(Long semesterId) {
        return classRoomRepository.findBySemesterId(semesterId);
    }
    @Override
    public List<ClassRoom> findClassRoomsBySemesterName(String semesterName) {
        return classRoomRepository.findBySemesterName(semesterName);
    }
    @Override
    public List<ClassRoom> findClassRoomsBySemesterIdAndTeacherId(Long semesterId, Long teacherId) {
        return classRoomRepository.findBySemesterIdAndTeacherId(semesterId, teacherId);
    }
}
