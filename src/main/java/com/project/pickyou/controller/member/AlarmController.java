package com.project.pickyou.controller.member;


import com.project.pickyou.dto.AlarmDTO;
import com.project.pickyou.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/alarm/*")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/list/{id}")  //일반쪽지 리스트
    public String alarmList(Model model, Principal pc, @PathVariable String id,  @RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
        String prid = pc.getName();
        if(id.equals(prid)){

           alarmService.findList(model, id, pageNum);  //본인의 쪽지 받은 목록확인
            model.addAttribute("id",id);
            return "/alarm/list";
        }else {
            return "/";
        }
    }


    @GetMapping("/list/{id}/content/{num}")  //일반회원쪽지 내용
    public String alarmcontent(Model model, Principal pc, @PathVariable String id,@PathVariable Long num){
        String prid = pc.getName();
        if(id.equals(prid)){

           alarmService.findContent(model,id, num);  //상세내용보기

            return "/alarm/content";
        }else {
            return "/";
        }
    }

    @GetMapping("/adminlist/{id}")  //어드민이 보낸 메세지
    public String adminSpend(Model model, Principal pc, @PathVariable String id,  @RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
        String prid = pc.getName();
        if(id.equals(prid)){

            alarmService.adminSpend(model, id, pageNum);  //관리자가 보낸쪽지목록
            model.addAttribute("id",id);
            return "/alarm/adminList";
        }else {
            return "/";
        }
    }

    @GetMapping("/adminlist/{id}/content/{num}")  //관리자가 보낸 쪽지상세내용
    public String adminalarmcontent(Model model, Principal pc, @PathVariable String id,@PathVariable Long num){
        String prid = pc.getName();
        if(id.equals(prid)){

            alarmService.adminSpendContent(model,id, num);  //상세내용보기

            return "/alarm/adminContent";
        }else {
            return "/";
        }
    }

    //쪽지쓰는곳
    @GetMapping("/alarmWrite/{id}")
    public String alarmWrite(Model model,Principal pc, @PathVariable String id){ //쪽지 쓰기
        String prid = pc.getName();
        if(id.equals(prid)){
            model.addAttribute("id",id);
            return "/alarm/alarmWrite";
        }else {
            return "/";
        }
    }

    //쪽지작성완료
    @PostMapping("/alarmWritePro/{id}")
    public String alarmWritePro(Principal pc, @PathVariable String id, @RequestParam("message") String message,
                                @RequestParam ("type") String type,
                                @RequestParam("name") String name) {

        //type 1:전체 2:사업자 3:일반회원 4:특정회원

        String prid = pc.getName();
        if(id.equals(prid)){
            if(type.equals("1") || type.equals("2") || type.equals("3")){
                alarmService.insertManyUser(id, name, message, type);
            }else {
                alarmService.insertOneUser(id, name, message);
            }
            return "redirect:/alarm/adminlist/" + id;
        }else {
            return "/";
        }


    }



}
