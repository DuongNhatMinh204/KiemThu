package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.Degree;
import com.nminh.kiemthu.model.request.DegreeCreateDTO;

import java.util.List;

public interface DegreeService {
    Degree createDegree(DegreeCreateDTO degreeCreateDTO);
    List<Degree> getAllDegrees();
    String deleteDegree(Long degreeId);
    Degree update(Long id , DegreeCreateDTO degreeCreateDTO);

    void setDegreeCoefficient(Long id, Double coefficient);
}
