package com.project.pickyou.controller.all;

import com.project.pickyou.service.PickService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pick/*")
public class PickController {

    private final PickService service;


    @GetMapping("/posts/my")
    public String myList(Model model, Principal principal,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum){

        if(principal!=null){
            service.myList(model, principal.getName(),pageNum);
            model.addAttribute("to",principal.getName());
            model.addAttribute("id",principal.getName());
        }

        return "pick/myList";
    }
}
