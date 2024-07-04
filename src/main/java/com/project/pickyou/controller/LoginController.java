package com.project.pickyou.controller;

import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import com.project.pickyou.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    //로그인 컨트롤러에서 -> 로그인, 회원가입, 에러, 로그아웃 처리를 진행중


    //로그인페이지
    @GetMapping("/login")
    public String loginProc() {

        return "login";
    }


    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }


    @GetMapping("/error")
    public String error() {
        String url = "redirect:/";
        return url;
    }



    //회원가입화면
    @GetMapping("/join")
    public String joinP(){

        return "join";
    }

    // 회원가입 테이터 처리
    @PostMapping("/joinProc")
    public String joinProcess(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO){

        loginService.joinProcess(memberDTO,memberInfoDTO);


        return "redirect:/login";
    }



}
