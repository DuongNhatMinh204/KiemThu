package com.nminh.kiemthu.controller;

import com.nminh.kiemthu.entity.User;
import com.nminh.kiemthu.model.request.UserLoginDTO;
import com.nminh.kiemthu.model.response.ApiResponse;
import com.nminh.kiemthu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse<User> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("User login : " + userLoginDTO);
        ApiResponse<User> apiReponse = new ApiResponse<>();
        User user = userService.login(userLoginDTO);
        apiReponse.setData(user);
        apiReponse.setMessage("login user success");
        if(user.getRoleName().equals("truongkhoa")) {
            apiReponse.setCode(8888);
            apiReponse.setMessage("login truong khoa success");
            log.info("truong khoa login success");
        }else if(user.getRoleName().equals("ketoan")) {
            apiReponse.setCode(6666);
            apiReponse.setMessage("login ketoan success");
            log.info("ketoan login success");
        }else {
            apiReponse.setCode(3333);
            apiReponse.setMessage("login giao vien success");
            log.info("ngay khoa login success");
        }
        return apiReponse;
    }

}
