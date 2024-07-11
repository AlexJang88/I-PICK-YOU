package com.project.pickyou.controller.all;

import com.project.pickyou.service.SatisfactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/satisfaction/*")
public class SatisfactionController {

        private final SatisfactionService service;

    @GetMapping("/score/{target}")
    public String score(Model model, @PathVariable String target, Principal principal, HttpServletRequest request){
        int check=0;
        System.out.println("===========target"+target);
        String url ="";
        String writer="";
        if(principal!=null){
            writer= principal.getName();
        }
        check = service.existCheck(writer,target,model);
        if(check==1){
         url="satisfaction/firstScore";
        }if(check==2){
            url="satisfaction/update";
        }if(check==3){
            url="redirect:/";
        }
        return url;
    }
    @GetMapping("/score/my")
    public String myscore(Model model,Principal principal){
        String id="";
        if(principal!=null){
            id= principal.getName();
        }

        return "satisfaction/myScore";
    }
}
