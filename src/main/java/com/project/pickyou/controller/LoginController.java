package com.project.pickyou.controller;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import com.project.pickyou.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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




    //아이디 찾기 페이지
    @GetMapping("/forgotId")
    public String forgotId(){

        return "/findUser/forgotId";
    }

    //아이디 찾음
    @PostMapping("/forgotId/idCheck")
    public String idCheck(String email, RedirectAttributes redirectAttributes){

        //이메일에 맞는 아이디 찾기
        String id = loginService.findId(email);
        redirectAttributes.addFlashAttribute("id", id);

        return "redirect:/forgotId/idfind";
    }

    //본인아이디 보여줌
    @GetMapping("/forgotId/idfind")
    public String idfind(Model model){

        if (model.containsAttribute("id")) {
            String id = (String) model.getAttribute("id");
            model.addAttribute("id",id);
        }
        return "/findUser/idfind";
    }




    //비밀번호 찾기 페이지
    @GetMapping("/forgotPw")
    public String forgotPw(){

        return "/findUser/forgotPw";
    }

    //비밀번호 변경전 멤버 확인
    @PostMapping("/forgotPw/pwCheck")
    public String pwCheck(String email, String id, RedirectAttributes redirectAttributes){

        Boolean pwCheck = loginService.checkPW(email,id);  //우선 회원의 여부
        System.out.println("><><<>><><"+pwCheck);

        if (pwCheck) {
            String sid = loginService.findId(email);  //비밀번호 바꾸는곳에 아이디도 넘김
            redirectAttributes.addFlashAttribute("sid", sid);
            return "redirect:/forgotPw/pwChange";
        } else { //그냥 문제사항 전달
            redirectAttributes.addFlashAttribute("error", "아이디 혹은 이메일이 올바르지 않습니다."); // Flash attribute를 이용해 에러 메시지 전달
            return "redirect:/forgotPw";
        }

    }

    //비밀번호 변경하는곳
    @GetMapping("/forgotPw/pwChange")
    public String pwChange(Model model){

        if (model.containsAttribute("sid")) {
            String id = (String) model.getAttribute("sid");
            model.addAttribute("id",id);
        }
            return "/findUser/pwChange";
    }

    //비밀번호 변경하는곳프로
    @PatchMapping("/forgotPw/pwChangePro")
    public String pwChangePro(MemberDTO memberDTO,@RequestParam("newPw") String newPw){
        loginService.changePw(memberDTO, newPw);  //아이디에 맞춰 비번 변경
        return "/login";
    }


}
