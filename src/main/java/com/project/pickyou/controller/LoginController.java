package com.project.pickyou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/login/*")
public class LoginController {

    /*로그인페이지*/
    @GetMapping("/loginmain")
    public String loginmain(){

        return "/login/loginmain";
    }

    /*로그인하기*/
    @PostMapping("/loginpro")
    public String loginpro(@RequestParam ("id") String id, @RequestParam ("pw") String pw){




        return "redirect:/main/pickyou";
    }




    /* 회원가입 페이지 */
    @GetMapping("/memberinput")
    public String memberinput(){

        return "login/memberinput"; // templates/login/memberinput.html 파일을 반환
    }

}
