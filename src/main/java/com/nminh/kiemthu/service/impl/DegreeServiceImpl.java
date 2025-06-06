package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.Degree;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.DegreeCreateDTO;
import com.nminh.kiemthu.repository.DegreeRepository;
import com.nminh.kiemthu.service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DegreeServiceImpl implements DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;

    @Override
    public Degree createDegree(DegreeCreateDTO degreeCreateDTO) {
        Degree degree = new Degree();
        List<Degree> degrees = degreeRepository.findAll();
        for (Degree degree1 : degrees) {
            if(degree1.getFullName().equals(degreeCreateDTO.getFullName())) {
                throw new AppException(ErrorCode.FULL_NAME_DEGREE_EXISTS);
            }
            if(degree1.getShortName().equals(degreeCreateDTO.getShortName())) {
                throw new AppException(ErrorCode.SHORT_NAME_DEGREE_EXISTS);
            }
        }
        for(int i = 0 ; i < degreeCreateDTO.getShortName().length() ; i++) {
            char ch = degreeCreateDTO.getShortName().charAt(i);
            if(Character.isDigit(ch)) {
                throw new AppException(ErrorCode.SHORT_NAME_NOT_VALID);
            }
        }
        for(int i = 0 ; i < degreeCreateDTO.getFullName().length() ; i++) {
            char ch = degreeCreateDTO.getFullName().charAt(i);
            if(Character.isDigit(ch)) {
                throw new AppException(ErrorCode.FULL_NAME_NOT_VALID);
            }
        }
        degree.setShortName(degreeCreateDTO.getShortName());
        degree.setFullName(degreeCreateDTO.getFullName());
        degree.setDegreeCoefficient(0.0);

        return degreeRepository.save(degree);
    }

    @Override
    public List<Degree> getAllDegrees() {
        if (degreeRepository.count() == 0) {
            throw new AppException(ErrorCode.NOT_EXISTS_DEGREE);
        }
        return degreeRepository.findAll();
    }

    @Override
    public String deleteDegree(Long degreeId) {
        degreeRepository.deleteById(degreeId);
        return "Degree deleted";
    }

    @Override
    public Degree update(Long id, DegreeCreateDTO degreeCreateDTO) {
        Degree degree = degreeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEGREE_NOT_FOUND));
        List<Degree> degrees = degreeRepository.findAll();
        for (Degree degree1 : degrees) {
            if(degree1 == degree) {
                continue;
            }
            if(degree1.getFullName().trim().equals(degreeCreateDTO.getFullName().trim())) {
                throw new AppException(ErrorCode.FULL_NAME_DEGREE_EXISTS);
            }
            if(degree1.getShortName().trim().equals(degreeCreateDTO.getShortName().trim())) {
                throw new AppException(ErrorCode.SHORT_NAME_DEGREE_EXISTS);
            }
        }
        for(int i = 0 ; i < degreeCreateDTO.getShortName().length() ; i++) {
            char ch = degreeCreateDTO.getShortName().charAt(i);
            if(Character.isDigit(ch)) {
                throw new AppException(ErrorCode.SHORT_NAME_NOT_VALID);
            }
        }
        for(int i = 0 ; i < degreeCreateDTO.getFullName().length() ; i++) {
            char ch = degreeCreateDTO.getFullName().charAt(i);
            if(Character.isDigit(ch)) {
                throw new AppException(ErrorCode.FULL_NAME_NOT_VALID);
            }
        }
        degree.setShortName(degreeCreateDTO.getShortName());
        degree.setFullName(degreeCreateDTO.getFullName());

        return degreeRepository.save(degree);
    }

    @Override
    public void setDegreeCoefficient(Long id, Double coefficient){
        Degree degree = degreeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEGREE_NOT_FOUND));
        degree.setDegreeCoefficient(coefficient);
        degreeRepository.save(degree);
    }
}