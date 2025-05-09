package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.model.request.SemesterCreateDTO;
import com.nminh.kiemthu.repository.SemesterRepository;
import com.nminh.kiemthu.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public Semester createSemester(SemesterCreateDTO semesterCreateDTO) {
        Semester semester = new Semester();

        semester.setSemesterName(semesterCreateDTO.getSemesterName());
        semester.setSchoolYear(semesterCreateDTO.getSchoolYear());
        semester.setTimeBegin(semesterCreateDTO.getTimeBegin());
        semester.setTimeEnd(semesterCreateDTO.getTimeEnd());

        return semesterRepository.save(semester);
    }

    @Override
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }
}
