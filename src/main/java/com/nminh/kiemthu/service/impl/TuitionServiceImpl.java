package com.nminh.kiemthu.service.impl;
import com.nminh.kiemthu.entity.Tuition;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.repository.TuitionRepository;
import com.nminh.kiemthu.service.TutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TuitionServiceImpl implements TutionService{
    @Autowired
    private TuitionRepository tuitionRepository;

    @Override
    public Tuition createTuition(Tuition tuition) {
        tuitionRepository.save(tuition);
        return tuition;
    }

    @Override
    public Tuition updateTuition(Long id, Long money) {
        if (money <= 0) {
            throw new IllegalArgumentException("Money cannot be negative");
        }
        Tuition tuition = tuitionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TUITION_NOT_FOUND));
        tuition.setPre_money(tuition.getMoney());
        tuition.setMoney(money);
        tuitionRepository.save(tuition);
        return tuition;
    }

    @Override
    public List<Tuition> getAll() {
        List<Tuition> tuitions = tuitionRepository.findAll();
        return tuitions;
    }
}
