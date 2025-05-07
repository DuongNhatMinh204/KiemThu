package com.nminh.kiemthu.service.impl;

import com.nminh.kiemthu.entity.User;
import com.nminh.kiemthu.enums.ErrorCode;
import com.nminh.kiemthu.exception.AppException;
import com.nminh.kiemthu.model.request.UserLoginDTO;
import com.nminh.kiemthu.repository.UserRepository;
import com.nminh.kiemthu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        if(!user.getPassword().equals(userLoginDTO.getPassword())){
            throw new AppException(ErrorCode.PASSWORD_ERROR) ;
        }
        return user;
    }
}
