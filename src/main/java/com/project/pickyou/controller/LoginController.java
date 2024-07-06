package com.project.pickyou.controller;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import com.project.pickyou.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


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

    @GetMapping("/loginProc")
    public String lod(){return "/sample/index1";}


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



    /*아래는 일반 회원가입 경로 및 화면*/
    //일반 회원가입화면
    @GetMapping("/join")
    public String joinP(){

        return "join";
    }

    // 일반회원가입 테이터 처리
    @PostMapping("/joinProc")
    public String joinProcess(MemberDTO memberDTO,
                              MemberInfoDTO memberInfoDTO,
                              CompanyInfoDTO companyInfoDTO,
                              @RequestParam("file") MultipartFile file) {

        if (companyInfoDTO.getCorpno()==null) {
            loginService.joinProcess(memberDTO, memberInfoDTO, file);
        }else {
            loginService.joinCompanyProcess(memberDTO, companyInfoDTO, file);
        }
        return "redirect:/login";
    }
    /*위에는 일반 회원가입 경로 및 화면*/


    /*아래는 사업자 회원가입 경로 및 화면*/

    @GetMapping("/joinCompany")
    public String joinCompany(){

        return "joinCompany";
    }

    // 사업자회원가입 테이터 처리
    @PostMapping("/joinCompanyPro")
    public String joinCompanyPro(MemberDTO memberDTO, CompanyInfoDTO companyInfoDTO, @RequestParam("file") MultipartFile file){

        System.out.println("=====><>><><><><>"+memberDTO.getAddress());
        System.out.println("=====><>><><><><>"+memberDTO.getPhone());
        System.out.println("=====><>><><><><>"+memberDTO.getProfile());
        System.out.println("=====><>><><><><>"+memberDTO.getId());

        System.out.println("=====><>><><><><>"+companyInfoDTO.getId());
        System.out.println("=====><>><><><><>"+companyInfoDTO.getName());
        System.out.println("=====><>><><><><>"+companyInfoDTO.getCompanyName());
        System.out.println("=====><>><><><><>"+companyInfoDTO.getCorpno());
        System.out.println("=====><>><><><><>"+companyInfoDTO.getJob());
        loginService.joinCompanyProcess(memberDTO, companyInfoDTO, file);

        return "redirect:/login";
    }





    /*위에는 사업자 회원가입 경로 및 화면*/






}
