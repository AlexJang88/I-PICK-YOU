package com.project.pickyou.controller.member;

import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/mypage/*")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Autowired
    private HttpSession session;



    /*일반유저 기준*/
    @GetMapping("/information/{id}")
    public String myInformation(Principal pc, Model model,@PathVariable String id) {
        String prid = pc.getName();
        if (prid.equals(id)) {
            memberService.findUserInfo(id, model);//회원정보 담아서 쏘기
            return "/mypage/myPage";
        } else {
            return "/login";   //없으면 로그인
        }
    }

    @GetMapping("/deleteUser/{id}")
    public String DeleteUser(@PathVariable String id,Model model, Principal pc){
        String prid =pc.getName();

        if(id.equals(prid)){
            model.addAttribute("id",id);
            return "/mypage/DeleteUser";
        } else {
            return "/";
        }
    }

    @DeleteMapping("/deleteuserPro")
    public String deleteuserPro(MemberDTO memberDTO){

        memberService.deleteUser(memberDTO);
        session.invalidate();  //세션날리기
        return "redirect:/login";
    }
    /*일반유저 기준*/

    /*사업자유저 기준*/
    @GetMapping("/cominformation/{id}")
    public String comInformation(Principal pc, Model model,@PathVariable String id) {
        String prid = pc.getName();
        if (prid.equals(id)) {
            memberService.findUserInfo(id, model);//회원정보 담아서 쏘기
            return "/mypage/companyMyPage";
        } else {
            return "/login";   //없으면 로그인
        }
    }
    /*사업자유저 기준*/


}





