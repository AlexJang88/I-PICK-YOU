package com.project.pickyou.controller.admin;

import com.project.pickyou.dto.PointDTO;
import com.project.pickyou.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/*")
public class AdminController {

    public final AdminService adminService;

    // 포인트 지급 내역
    @GetMapping("/point/give")
    public String giveList(Model model,
                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(value = "month", defaultValue = "1") int month) {

       System.out.println("month--------------------------------------------------"+month);

       adminService.AllPosts(model, 1, pageNum);
       return "admin/pointGiveList";
    }

    // 포인트 차감 내역
    @GetMapping("/point/deduct")
    public String deductList(Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
        adminService.AllPost(model, 2, pageNum);
        return "admin/pointDeductList";
    }


    @GetMapping("/management")
    public String Management(Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Principal pc){
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }

        adminService.getUsre(model, pageNum); //일반회원 정보 가져오기
        return "admin/management";
    }

    @DeleteMapping("/userDelete")
    public String userDelete(String id, Principal pc, Model model){ //일반유저삭제
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }
        adminService.userDelete(id);
        return "redirect:/admin/management";
    }


    @GetMapping("/companyManagement")
    public String companyManagement(Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Principal pc){
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }
        adminService.getCompany(model, pageNum); //사업자회원 정보 가져오기
        return  "admin/companyManagement";
    }

    @DeleteMapping("/companyDelete")
    public String companyDelete(String id, Principal pc, Model model){ //사업자유저삭제
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }
        adminService.userDelete(id);
        return "redirect:/admin/companyManagement";
    }

    @GetMapping("/pointPayment")
    public String pointPayment(Principal pc, Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum){ //포인트 승인 하는곳
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }

        adminService.getnPoitApproval(model, pageNum); //포인트 승인 요청내역 가져오기

        return "admin/pointPayment";
    }

    @PatchMapping("/userPointPatch")
    public String userPointPatch (Principal pc, Model model, PointDTO pointDTO){ //포인트 차감으로 변경해주기
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }
        System.out.println("><><><><><"+pointDTO.getMemberId());
        System.out.println("><><><><><"+pointDTO.getId());

        adminService.patchPoint(pointDTO);

        return "redirect:/admin/pointPayment";
    }




}
