package com.project.pickyou.controller.all;

import com.project.pickyou.dto.SatisfactionDTO;
import com.project.pickyou.service.SatisfactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/satisfaction/*")
public class SatisfactionController {

        private final SatisfactionService service;


        @GetMapping("/score")
        public String newList(Model model,Principal principal,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
            String sid="";
            if(principal!=null){
                sid= principal.getName();
                model.addAttribute("id",principal.getName());
                service.scoreList(model,sid,pageNum);
            }
            return "satisfaction/list";
        }



        @GetMapping("/score/{target}")
    public String score(Model model, @PathVariable String target, Principal principal, HttpServletRequest request){
        int check=0;
        System.out.println("===========target"+target);
        String url ="";
        String writer="";
        if(principal!=null){
            writer= principal.getName();
            model.addAttribute("id",principal.getName());
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
    @PostMapping("/score/new")
    public String scoreNew(SatisfactionDTO dto,Principal principal){
        String writer="";
        if(principal!=null){
            writer= principal.getName();
        }
        dto.setWriter(writer);
        service.scoreSet(dto);
        return "redirect:/";
    }
    @PatchMapping("/score/edit")
    public String scoreEdit(SatisfactionDTO dto,Principal principal){
        String writer="";
        if(principal!=null){
            writer= principal.getName();
        }
        dto.setWriter(writer);
        service.scoreEdit(dto);
        return "redirect:/";
    }


    @GetMapping("/score/my")
    public String myscore(Model model,Principal principal){
        String id="";
        if(principal!=null){
            id= principal.getName();
            model.addAttribute("id",principal.getName());
        }
        service.myScore(model,id,1);
        return "satisfaction/myScore";
    }
}
