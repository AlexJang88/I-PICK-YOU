package com.project.pickyou.controller.all;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trainning")
public class TrainningController {

    @GetMapping("/list")
    public String trainningmain(){

        return "/trainning/trainningmain";
    }

}
