package com.nminh.kiemthu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/admin")
    public String admin() {
        return "truongkhoa";
    }
    @GetMapping("/ketoan")
    public String ketoan(){
        return "salary-management";
    }
}
