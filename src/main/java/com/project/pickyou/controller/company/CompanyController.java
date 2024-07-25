package com.project.pickyou.controller.company;

import com.project.pickyou.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company/*")
public class CompanyController {
    private final MemberService memberService;

    @GetMapping("/payment/posts")
    public String posts(Model model, Principal principal,
                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@
        memberService.paymentList(model, pageNum, principal.getName(), 1);
        return "mypage/paymentList";
    }
}
