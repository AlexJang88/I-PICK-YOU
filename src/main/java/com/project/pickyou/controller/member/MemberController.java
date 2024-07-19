package com.project.pickyou.controller.member;

import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import com.project.pickyou.service.MemberService;
import com.project.pickyou.service.PointService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/mypage/*")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PointService pointService;

    @Autowired
    private HttpSession session;



    /*일반유저 기준*/
    @GetMapping("/information/{id}")
    public String myInformation(Principal pc, Model model,@PathVariable String id) {
        String prid = pc.getName();
        if (prid.equals(id)) {
            memberService.findUserInfo(id, model);//회원정보 담아서 쏘기
            return "mypage/myPage";
        } else {
            return "login";   //없으면 로그인
        }
    }

    //회원삭제
    @GetMapping("/deleteUser/{id}")
    public String DeleteUser(@PathVariable String id,Model model, Principal pc){
        String prid =pc.getName();

        if(id.equals(prid)){
            model.addAttribute("id",id);
            return "mypage/DeleteUser";
        } else {
            return "/";
        }
    }
    //회원삭제 프로
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
            return "mypage/companyMyPage";
        } else {
            return "login";   //없으면 로그인
        }
    }
    /*사업자유저 기준*/



    //일반 회원 수정
    @GetMapping("/userUpdate/{id}")
    public String userUpdate(Principal pc, Model model,@PathVariable String id){

        String prid = pc.getName();
        if (prid.equals(id)) {   //수정하기 버튼 누를때
            memberService.findUserInfo(id, model);//회원정보 담아서 쏘기
            return "mypage/userUpdate";
        } else {
            return "login";   //없으면 로그인
        }
    }

    //일반유저정보수정
    @PatchMapping("/userUpdatePro/{id}")
    public String userUpdatePro(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO, @RequestParam("file") MultipartFile file){

        String id = memberDTO.getId();

        memberService.updateUser(memberDTO,memberInfoDTO,file); //회원정보수정

        return "redirect:/mypage/information/"+id;
    }


    //사업자 회원 수정

    @GetMapping("/companyUpdate/{id}")
    public String companyUpdate(Principal pc, Model model,@PathVariable String id){

        String prid = pc.getName();
        if (prid.equals(id)) {   //수정하기 버튼 누를때
            memberService.findUserInfo(id, model);//회원정보 담아서 쏘기
            return "mypage/companyUpdate";
        } else {
            return "login";   //없으면 로그인
        }

    }

    //일반유저정보수정
    @PatchMapping("/companyUpdatePro/{id}")
    public String companyUpdatePro(MemberDTO memberDTO, CompanyInfoDTO companyInfoDTO, @RequestParam("file") MultipartFile file){

        String id = memberDTO.getId();

       memberService.updateCompany(memberDTO,companyInfoDTO,file); //사업자 정보수

        return "redirect:/mypage/cominformation/"+id;
    }

    // 포인트 적립 내역
    @GetMapping("/point/plus/posts")
    public String pointPlusList(Principal principal, Model model,
                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {

        pointService.AllPosts(model, pageNum, principal.getName());

        return "mypage/pointList";
    }

    // 포인트 차감 내역
    @GetMapping("/point/minus/posts")
    public String pointMinusList(Principal principal, Model model,
                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {

        pointService.AllPosts(model, pageNum, principal.getName());

        return "mypage/pointMinusList";
    }



    // 일반회원의 지원현황
    @GetMapping("recruit/posts")
    public String recruitStateList(Principal principal, Model model,
                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {

        memberService.userRecruitList(principal.getName(), pageNum, model);
        return "mypage/recruitStateList";
    }

    // 유저입장에서 채용현황
    @GetMapping("confirm/posts")
    public String userConfirmList(Principal principal, Model model,
                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {

        memberService.confirmList(principal.getName(), pageNum, model);
        return "mypage/userConfirmList";
    }














}





