package com.project.pickyou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String lo(){
        return "login";
    }

    @GetMapping("/loginProc")
    public String lod(){return "/sample/index1";}


}
