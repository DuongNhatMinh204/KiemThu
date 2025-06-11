package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Tuition;
import org.springframework.stereotype.Service;

import java.util.List;
public interface TutionService {
    Tuition createTuition(Tuition tuition);
    Tuition updateTuition(Long id, Long money);
    List<Tuition> getAll();
}
