package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.constants.Constant;
import com.nminh.kiemthu.entity.*;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.ClassRoomCreateDTO;
import com.nminh.kiemthu.model.request.ClassRoomUpdateRequest;
import com.nminh.kiemthu.repository.*;
import com.nminh.kiemthu.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<ClassRoom> createClassroom(ClassRoomCreateDTO classRoomCreateDTO) {
        Semester semester = semesterRepository.findById(classRoomCreateDTO.getSemesterId())
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        Subject subject = subjectRepository.findById(classRoomCreateDTO.getSubjectId())
                .orElseThrow(()-> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        if (classRoomCreateDTO.getNumberOfClasses() <= 0) {
            throw new AppException(ErrorCode.NUMBER_OF_CLASSES_NOT_VALID);
        }
        if (classRoomCreateDTO.getNumberOfStudents() <= 0) {
            throw new AppException(ErrorCode.NUMBER_OF_STUDENT_NOT_VALID);
        }
        List<ClassRoom> classRooms = new ArrayList<>();
        for(int i = 0 ; i < classRoomCreateDTO.getNumberOfClasses() ; i++) {
            ClassRoom classRoom = new ClassRoom(classRoomCreateDTO.getNumberOfStudents() ,  semester , subject);
            String className = determineClassName(subject.getSubjectName())+ (i+1) ;
            classRoom.setClassName(className);
            classRoomRepository.save(classRoom);
            classRooms.add(classRoom);

        }
        return classRooms;
    }

    private String determineClassName(String subjectName ){
        if (subjectName == null || subjectName.trim().isEmpty()) {
            return "";
        }

        // Tách từng từ ra bằng khoảng trắng
        String[] words = subjectName.trim().split("\\s+");
        StringBuilder res = new StringBuilder();

        // Lấy chữ cái đầu tiên của mỗi từ và viết hoa
        for (String word : words) {
            if (!word.isEmpty()) {
                res.append(Character.toUpperCase(word.charAt(0)));
            }
        }

        return res.toString();
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

    @Override
    public String deleteClassroom(Long classroomId) {
        ClassRoom classRoom = classRoomRepository.findById(classroomId)
                .orElseThrow(()->new AppException(ErrorCode.CLASS_NOT_FOUND)) ;
        classRoomRepository.delete(classRoom);
        return "Classroom deleted";
    }

    @Override
    public ClassRoom changeClassRoom(Long id, ClassRoomUpdateRequest classRoomUpdateRequest) {
        ClassRoom classRoom = classRoomRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CLASS_NOT_FOUND));
        Teacher teacher = teacherRepository.findById(classRoomUpdateRequest.getTeacherId())
                        .orElseThrow(()-> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        if(classRoomUpdateRequest.getNumberOfStudents() <= 0) {
            throw  new AppException(ErrorCode.NUMBER_OF_STUDENT_NOT_VALID) ;
        }
        classRoom.setNumberOfStudents(classRoomUpdateRequest.getNumberOfStudents());
        classRoom.setTeacher(teacher);
        int numberOfStudents = classRoomUpdateRequest.getNumberOfStudents();
        Double coe ;
        if(numberOfStudents>0 && numberOfStudents < 20) {
             coe = Constant.NUMBER_STUDENT_LESS_THAN_TWENTY ;
        } else if(numberOfStudents>= 20 && numberOfStudents <= 29) {
            coe = Constant.NUMBER_STUDENT_LESS_THAN_TWENTY_NINE ;
        }else if(numberOfStudents>= 30 && numberOfStudents <= 39) {
            coe = Constant.NUMBER_STUDENT_LESS_THAN_THIRTY_NINE;
        } else if (numberOfStudents >= 40 && numberOfStudents <= 49) {
            coe = Constant.NUMBER_STUDENT_LESS_THAN_FOURTY_NINE;
        }else if (numberOfStudents >= 50 && numberOfStudents <= 59) {
            coe = Constant.NUMBER_STUDENT_LESS_THAN_FIFTY_NINE;
        }else if (numberOfStudents >= 60 && numberOfStudents <= 69) {
            coe = Constant.NUMBER_STUDENT_LESS_THAN_SIXTY_NINE ;
        }else if (numberOfStudents >= 70 && numberOfStudents <= 79) {
            coe = Constant.NUMBER_STUDENT_LESS_THAN_SEVENTIES_NINE ;
        }else {
            coe = (double) 0;
        }
        classRoom.setClassCoefficient(coe);
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
