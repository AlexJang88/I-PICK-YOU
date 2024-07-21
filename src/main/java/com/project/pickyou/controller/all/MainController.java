package com.project.pickyou.controller.all;


import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.service.LoginService;
import com.project.pickyou.service.NoticeService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;

@Controller
@RequiredArgsConstructor
public class MainController {

    // 정룡 (메인에 공지사항)
    private final NoticeService noticeService;

    @GetMapping("/")
    public String mainp(Model model){


        //아래 코드는 메인에서 로그인시 어떤 권한을가지고 있는지 확인하다
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();
        model.addAttribute("id",id);
        model.addAttribute("role",role);

        // 정룡 (메인에 공지사항)
        noticeService.mainNotice(model);

        return "main";
    }

    @GetMapping("/calendar")
    public String calendar(Principal principal,Model model){
        String sid="";
        if(principal!=null){
            sid=principal.getName();
            model.addAttribute("memberId",sid);
            model.addAttribute("id",principal.getName());
        }
        return "calendar/calendar";
    }




}
