package com.project.pickyou.controller.admin;


import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import com.project.pickyou.entity.PaymentEntity;
import com.project.pickyou.dto.PointDTO;
import com.project.pickyou.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/*")
public class AdminController {

    public final AdminService adminService;

    // 포인트 지급 내역
    @GetMapping("/point/give")
    public String giveList(Model model, Principal principal,
                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(value = "month", required = false) Integer month,
                           @RequestParam(value = "year", required = false) Integer year) {

        // 현재 연도와 월을 구합니다.
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // 연도와 월이 null인 경우 현재 연도와 월로 설정합니다.
        if (year == null) {
            year = currentYear;
        }
        if (month == null) {
            month = currentMonth;
        }

        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("years", adminService.generateYearList());

        adminService.AllPostsGIVE(model, 1, pageNum, year, month);
       return "admin/pointGiveList";
    }

    // 포인트 지급 내역(몇월인지 값받아오기)
    @GetMapping("/point/give/month")
    public String monthGIVE(Model model, @RequestParam("month") int month,
                            @RequestParam("year") int year,
                            RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("month", month);
        redirectAttributes.addAttribute("year", year);
        return "redirect:/admin/point/give";
    }





    @GetMapping("/point/deduct")
    public String deductList(Model model, Principal principal,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "month", required = false) Integer month,
                             @RequestParam(value = "year", required = false) Integer year) {

        // 현재 연도와 월을 구합니다.
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // 연도와 월이 null인 경우 현재 연도와 월로 설정합니다.
        if (year == null) {
            year = currentYear;
        }
        if (month == null) {
            month = currentMonth;
        }

        if (principal != null) {
            model.addAttribute("id", principal.getName());
        }

        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("years", adminService.generateYearList()); // 연도 목록 추가

        adminService.AllPostsGIVE(model, 2, pageNum, year, month);
        return "admin/pointDeductList";
    }

    @GetMapping("/point/deduct/month")
    public String monthDEDUCT(@RequestParam("month") int month,
                              @RequestParam("year") int year,
                              RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("month", month);
        redirectAttributes.addAttribute("year", year);
        return "redirect:/admin/point/deduct";
    }

    /*// 포인트 사용 내역
    @GetMapping("/point/deduct")
    public String deductList(Model model, Principal principal,
                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(value = "month", defaultValue = "1") int month) {

        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        model.addAttribute("month", month);
        adminService.AllPostsGIVE(model, 2, pageNum, month);
        return "admin/pointDeductList";
    }

    // 포인트 사용 내역(몇월인지 값받아오기)
    @GetMapping("/point/deduct/month")
    public String monthDEDUCT(Model model,
                              @RequestParam("month") int month, RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("month", month);
        return "redirect:/admin/point/deduct";
    }*/


    // 페이 결제 내역
    @GetMapping("/payment/posts")
    public String paymentList(Model model, Principal principal,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "month", required = false) Integer month,
                              @RequestParam(value = "year", required = false) Integer year) {

        // 현재 연도와 월을 구합니다.
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // 연도와 월이 null인 경우 현재 연도와 월로 설정합니다.
        if (year == null) {
            year = currentYear;
        }
        if (month == null) {
            month = currentMonth;
        }

        if (principal != null) {
            model.addAttribute("id", principal.getName());
        }

        // 사용자 정보 추가
        if (principal != null) {
            model.addAttribute("id", principal.getName());
        }

        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("years", adminService.generateYearList()); // 연도 목록 추가

        adminService.AllpaymentANDpoint(model, 1, pageNum, year, month);
        return "admin/paymentList";
    }

    // 페이 결제 내역(몇월인지 값받아오기)
    @GetMapping("/payment/month")
    public String paymentListMonth(Model model,
                                   @RequestParam("month") int month,
                                   @RequestParam("year") int year,
                                   RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("month", month);
        redirectAttributes.addAttribute("year", year);
        return "redirect:/admin/payment/posts";
    }



    // 달력 총 매출, 순이익 내역
    @GetMapping("/payment/month/totalRevenue")
    public String salesInfoMonth(Model model,
                                 @RequestParam("chartType") String chartType,
                                 @RequestParam("month") int month,
                                 @RequestParam(value = "year", defaultValue = "0") int year, // 기본값을 0으로 설정
                                 RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("month", month);
        redirectAttributes.addAttribute("year", year); // 연도도 전달
        redirectAttributes.addAttribute("chartType", chartType);


        return "redirect:/admin/payment/totalRevenue";
    }

    // 총 매출, 순이익 내역
    @GetMapping("/payment/totalRevenue")
    public String salesInfo(Model model, Principal principal,
                            @RequestParam(value = "month", required = false) Integer month,
                            @RequestParam(value = "year", required = false) Integer year, // 기본값을 0으로 설정
                            @RequestParam(value = "chartType", defaultValue = "both") String chartType) {

        // 현재 연도와 월을 구합니다.
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // 연도와 월이 null인 경우 현재 연도와 월로 설정합니다.
        if (year == null) {
            year = currentYear;
        }
        if (month == null) {
            month = currentMonth;
        }

        if (principal != null) {
            model.addAttribute("id", principal.getName());
        }

        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("chartType", chartType);
        model.addAttribute("years", adminService.generateYearList()); // 연도 목록 추가
        // Retrieve and process data for the selected month and year
        adminService.salesInfo(model, 1, year, month, chartType);

        return "admin/totalRevenue";
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
    public String userDelete(String memberId, Principal pc, Model model){ //일반유저삭제
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }

        adminService.userDelete(memberId);
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
    public String companyDelete(String memberId, Principal pc, Model model){ //사업자유저삭제
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }

        adminService.userDelete(memberId);
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
