package com.project.pickyou.controller.all;

import com.project.pickyou.service.RecruitStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruitState/*")
public class RecruitStateController {

    private final RecruitStateService service;

    @GetMapping("/posts/apply/{recruitId}")
    public String statePosts(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @PathVariable Long recruitId){
        String sid="two";
        service.onApply(model,pageNum,recruitId);
        return "recruitState/applyList";
    }
    @GetMapping("/posts/confirm/{recruitId}")
    public String stateConfirm(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @PathVariable Long recruitId){
        String sid="two";
        service.confirmedMember(model,pageNum,recruitId);
        return "recruitState/confirmedList";
    }
    @DeleteMapping("/posts/confirm/{memberId}/{recruitId}")
    public String deleteconfirm(@PathVariable String memberId,@PathVariable Long recruitId){
        service.cancelConfirmed(memberId,recruitId);
        String url = "redirect:/recruitState/apply/"+recruitId;
    return url;
    }


}
