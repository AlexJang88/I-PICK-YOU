package com.project.pickyou.controller.admin;

import com.project.pickyou.entity.PaymentEntity;
import com.project.pickyou.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/*")
public class AdminController {

    public final AdminService adminService;

    // 포인트 지급 내역
    @GetMapping("/point/give")
    public String giveList(Model model, Principal principal,
                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(value = "month", defaultValue = "7") int month) {

        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        model.addAttribute("month", month);
       adminService.AllPostsGIVE(model, 1, pageNum, month);
       return "admin/pointGiveList";
    }

    // 포인트 지급 내역(몇월인지 값받아오기)
    @GetMapping("/point/give/month")
    public String monthGIVE(Model model, @RequestParam("month") int month, RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("month", month);
        return "redirect:/admin/point/give";
    }

    // 포인트 사용 내역
    @GetMapping("/point/deduct")
    public String deductList(Model model, Principal principal,
                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(value = "month", defaultValue = "7") int month) {

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
    }


    // 페이 결제 내역
    @GetMapping("/payment/posts")
    public String paymentList(Model model, Principal principal,
                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(value = "month", defaultValue = "7") int month) {

        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        model.addAttribute("month", month);
        adminService.AllpaymentANDpoint(model, 1, pageNum, month);
        return "admin/paymentList";
    }

    // 페이 결제 내역(몇월인지 값받아오기)
    @GetMapping("/payment/month")
    public String paymentListMonth(Model model,
                                   @RequestParam("month") int month, RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("month", month);
        return "redirect:/admin/payment/posts";
    }


    // 달력 총 매출, 순이익 내역
    @GetMapping("/payment/month/totalRevenue")
    public String TEST2(Model model,
                        @RequestParam("chartType") String chartType,
                        @RequestParam("month") int month, RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("month", month);
        redirectAttributes.addAttribute("chartType", chartType);

        return "redirect:/admin/payment/totalRevenue";
    }
    // 총 매출, 순이익 내역
    @GetMapping("/payment/totalRevenue")
    public String TEST(Model model, Principal principal,
                       @RequestParam(value = "month", defaultValue = "7") int month,
                       @RequestParam(value = "chartType", defaultValue = "both") String chartType) {

        if (principal != null) {
            model.addAttribute("id", principal.getName());
        }

        // Retrieve and process data for the selected month
        adminService.TEST(model, 1, month, chartType);

        // Process data to be used in the chart
        List<PaymentEntity> payments = (List<PaymentEntity>) model.getAttribute("posts");

        // Aggregate data by date
        Map<LocalDate, Integer> aggregatedData = payments.stream()
                .collect(Collectors.groupingBy(
                        post -> post.getReg().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.summingInt(PaymentEntity::getMoney)
                ));

        // Convert aggregated data to chart-friendly format
        List<Map<String, Object>> chartData = aggregatedData.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("x", entry.getKey().toString()); // Use LocalDate's default toString for formatting
                    dataPoint.put("y", entry.getValue());
                    return dataPoint;
                })
                .collect(Collectors.toList());

        model.addAttribute("chartData", chartData);
        model.addAttribute("month", month);
        return "admin/totalRevenue";
    }








    @GetMapping("/management")
    public String Management(Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum){
        adminService.getUsre(model, pageNum); //일반회원 정보 가져오기
        return "admin/management";
    }

    @DeleteMapping("/userDelete")
    public String userDelete(String id){ //일반유저삭제
        adminService.userDelete(id);
        return "redirect:/admin/management";
    }


    @GetMapping("/companyManagement")
    public String companyManagement(Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum){
        adminService.getCompany(model, pageNum); //사업자회원 정보 가져오기
        return  "admin/companyManagement";
    }

    @DeleteMapping("/companyDelete")
    public String companyDelete(String id){ //사업자유저삭제
        adminService.userDelete(id);
        return "redirect:/admin/companyManagement";
    }

}
