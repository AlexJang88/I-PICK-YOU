package com.project.pickyou.controller.all;

import com.project.pickyou.service.RecruitStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruitState/*")
public class RecruitStateController {

    private final RecruitStateService service;

    @GetMapping("/posts/apply/{recruitId}")
    public String statePosts(Principal principal,Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @PathVariable Long recruitId){
        String sid="";
        if(principal!=null) {
            sid= principal.getName();
            model.addAttribute("id",principal.getName());
            service.onApply(model, pageNum, recruitId, 3);
        }
        return "recruitState/applyList";
    }
    @GetMapping("/posts/confirm/{recruitId}")
    public String stateConfirm(Principal principal,Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @PathVariable Long recruitId){
        String sid="";
        if(principal!=null) {
            sid= principal.getName();
            service.confirmedMember(model, pageNum, recruitId, 3);
            model.addAttribute("id",principal.getName());
        }
        return "recruitState/confirmedList";
    }
    @GetMapping("/posts/my")
    public String myList(Model model, Principal principal,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
        String sid="";
        if(principal!=null){
            sid=principal.getName();
            service.myApply(model,sid,pageNum);
            model.addAttribute("id",principal.getName());
        }
        return "recruitState/myList";
    }
    @PostMapping("/posts")
    public String apply(@RequestParam(value = "boardNum")Long boardNum,Principal principal){
        String sid="";
        if(principal!=null){
            sid= principal.getName();
            service.Apply(sid,boardNum);
        }
        return "redirect:/recruit/posts/"+boardNum;
    }
    @PatchMapping("/posts")
    public  String patch(@RequestParam String memberId,@RequestParam Long recruitId){
                service.cancelConfirmed(memberId,recruitId,3);


    return "redirect:/recruitState/posts/confirm/"+recruitId;
    }
    @DeleteMapping("/posts")
    public String delete(Principal principal,@RequestParam Long recruitId){
        String url="";
        int check=0;
        if(principal!=null) {
            check = service.cancelApply(principal.getName(), recruitId);
            if (check == 1) {
                url = "redirect:/recruitState/posts/my";
            } else if (check == 2) {
                url = "redirect:/recruitState/posts/apply/" + recruitId;
            }
        }
        return url;
    }


}
