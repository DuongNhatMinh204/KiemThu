package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.Semester;
import com.nminh.kiemthu.entity.Tuition;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.SemesterCreateDTO;
import com.nminh.kiemthu.repository.SemesterRepository;
import com.nminh.kiemthu.repository.TuitionRepository;
import com.nminh.kiemthu.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private TuitionRepository tuitionRepository;
    @Override
    public Semester createSemester(SemesterCreateDTO semesterCreateDTO) {
        String schoolYear = semesterCreateDTO.getSchoolYear().trim();
        String[] years = schoolYear.split("-");

        if (years.length != 2) {
            throw new AppException(ErrorCode.SCHOOL_YEAR_INVALID);
        }

        try {
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);

            if (endYear - startYear != 1) {
                throw new AppException(ErrorCode.SCHOOL_YEAR_INVALID); // Bạn cần thêm mã lỗi này
            }
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.SCHOOL_YEAR_INVALID);
        }

        if(!semesterCreateDTO.getSemesterName().trim().startsWith("Học Kỳ")){
            throw new AppException(ErrorCode.SEMESTER_NAME_NOT_VALID) ;
        }
        if(!semesterCreateDTO.getSemesterName().trim().endsWith("1")
                && !semesterCreateDTO.getSemesterName().trim().endsWith("2")
                        && !semesterCreateDTO.getSemesterName().trim().endsWith("3")){
            throw new AppException(ErrorCode.SEMESTER_NAME_NOT_VALID) ;
        }
        Semester semester = new Semester();
        // find semester by school year
        List<Semester> semestersInSchoolYear = semesterRepository.findBySchoolYear(semesterCreateDTO.getSchoolYear());
        if(!semestersInSchoolYear.isEmpty()) {
            for (Semester s : semestersInSchoolYear) {
                //kiem tra ten
                if(semesterCreateDTO.getSemesterName().equals(s.getSemesterName())) {
                    throw new AppException(ErrorCode.EXISTS_SEMESTER_NAME) ;
                }
                // nếu tgian bắt đầu ở giữa thời gian bắt đầu kì  và  thời gian kết thúc kì khác
                if(semesterCreateDTO.getTimeBegin().isAfter(s.getTimeBegin()) && semesterCreateDTO.getTimeEnd().isBefore(s.getTimeEnd())) {
                    throw new AppException(ErrorCode.TIME_BEGIN_INVALID) ;
                }
                // nếu thời gian kết thúc ở giữa thời gian bắt đầu kì và thời gian kết thúc kì khác
                if(semesterCreateDTO.getTimeEnd().isAfter(s.getTimeBegin()) && semesterCreateDTO.getTimeBegin().isBefore(s.getTimeEnd())) {
                    throw new AppException(ErrorCode.TIME_END_INVALID) ;
                }
            }
        }

        semester.setSemesterName(semesterCreateDTO.getSemesterName());
        semester.setSchoolYear(semesterCreateDTO.getSchoolYear().trim());
        semester.setTimeBegin(semesterCreateDTO.getTimeBegin());
        semester.setTimeEnd(semesterCreateDTO.getTimeEnd());

        Tuition tuition = new Tuition();
        Long money = 0L;
        tuition.setMoney(money);
        tuition.setPre_money(money);
        tuition.setSemester(semester);

        semesterRepository.save(semester);
        tuitionRepository.save(tuition);
        return semester;
    }
    public List<Semester> findBySchoolYear(String schoolYear) {
        String schoolYearToFind = schoolYear.trim() ;
        return semesterRepository.findBySchoolYear(schoolYearToFind);
    }
    @Override
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    @Override
    public Semester updateSemester(Long id, SemesterCreateDTO semesterCreateDTO) {
        String schoolYear = semesterCreateDTO.getSchoolYear().trim();
        String[] years = schoolYear.split("-");

        if (years.length != 2) {
            throw new AppException(ErrorCode.SCHOOL_YEAR_INVALID);
        }

        try {
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);

            if (endYear - startYear != 1) {
                throw new AppException(ErrorCode.SCHOOL_YEAR_INVALID); // Bạn cần thêm mã lỗi này
            }
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.SCHOOL_YEAR_INVALID);
        }


        if(!semesterCreateDTO.getSemesterName().trim().startsWith("Học Kỳ")){
            throw new AppException(ErrorCode.SEMESTER_NAME_NOT_VALID) ;
        }
        if(!semesterCreateDTO.getSemesterName().trim().endsWith("1")
                && !semesterCreateDTO.getSemesterName().trim().endsWith("2")
                && !semesterCreateDTO.getSemesterName().trim().endsWith("3")){
            throw new AppException(ErrorCode.SEMESTER_NAME_NOT_VALID) ;
        }

        Semester semester = semesterRepository.findById(id).get();

        // find semester by school year
        List<Semester> semestersInSchoolYear = semesterRepository.findBySchoolYear(semesterCreateDTO.getSchoolYear());
        if(!semestersInSchoolYear.isEmpty()) {
            for (Semester s : semestersInSchoolYear) {
                if(semester.getId().equals(s.getId())) {
                    continue;
                }
                //kiem tra ten
                if(semesterCreateDTO.getSemesterName().equals(s.getSemesterName())) {
                    throw new AppException(ErrorCode.EXISTS_SEMESTER_NAME) ;
                }
                // nếu tgian bắt đầu ở giữa thời gian bắt đầu kì  và  thời gian kết thúc kì khác
                if(semesterCreateDTO.getTimeBegin().isAfter(s.getTimeBegin()) && semesterCreateDTO.getTimeEnd().isBefore(s.getTimeEnd())) {
                    throw new AppException(ErrorCode.TIME_BEGIN_INVALID) ;
                }
                // nếu thời gian kết thúc ở giữa thời gian bắt đầu kì và thời gian kết thúc kì khác
                if(semesterCreateDTO.getTimeEnd().isAfter(s.getTimeBegin()) && semesterCreateDTO.getTimeBegin().isBefore(s.getTimeEnd())) {
                    throw new AppException(ErrorCode.TIME_END_INVALID) ;
                }
            }
        }

        semester.setSemesterName(semesterCreateDTO.getSemesterName());
        semester.setSchoolYear(semesterCreateDTO.getSchoolYear());
        semester.setTimeBegin(semesterCreateDTO.getTimeBegin());
        semester.setTimeEnd(semesterCreateDTO.getTimeEnd());

        return semesterRepository.save(semester);
    }

    @Override
    public String deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id).get();
        semesterRepository.delete(semester);
        return "deleted";
    }

}
