package com.project.pickyou.controller.admin;

import com.project.pickyou.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
