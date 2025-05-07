package com.nminh.kiemthu.service;

import com.nminh.kiemthu.entity.User;
import com.nminh.kiemthu.model.request.UserLoginDTO;

public interface UserService {
    public User login(UserLoginDTO userLoginDTO);
}
