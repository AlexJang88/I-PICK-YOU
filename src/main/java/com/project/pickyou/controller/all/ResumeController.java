package com.project.pickyou.controller.all;

import com.project.pickyou.dto.ResumeDTO;
import com.project.pickyou.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume/*")
public class ResumeController {

    private final ResumeService resumeService;

    // 이력서 리스트
    @GetMapping("/posts")
    public String list(Principal principal, Model model) {
        resumeService.selectResume(model, principal.getName());
        return "resume/list";
    }

    // 이력서 작성
    @GetMapping("/posts/new")
    public String write() {
        return "resume/write";
    }

    // 이력서 인서트
    @PostMapping("/posts")
    public String writePro(ResumeDTO Rdto, Principal principal, String careerName,
                           @RequestParam("jobName") List<String> jobNames,
                           @RequestParam("licenceName") List<String> licenceNames,
                           @RequestParam("equipmentName") List<String> equipmentNames,
                           @RequestParam("certificationName") List<String> certificationNames) {

        // 이력서 인서트
        Rdto.setMemberId(principal.getName());
        resumeService.resumeInsert(Rdto, careerName, jobNames, licenceNames, equipmentNames, certificationNames);

        return "redirect:/resume/posts";
    }

    // 이력서 상세정보
    @GetMapping("/posts/{num}")
    public String info(@PathVariable Long num, Model model, Principal principal) {
        model.addAttribute("sessionId", principal.getName());
        resumeService.selectResumeInfo(model, num);
        return "resume/info";
    }

    // 이력서 삭제
    @DeleteMapping("/posts/{num}")
    public String delete(@PathVariable Long num) {
        resumeService.deleteResume(num);
        return "redirect:/resume/posts";
    }

    // 이력서 업데이트
    @GetMapping("/posts/{num}/edit")
    public String update(@PathVariable Long num, Model model) {

        resumeService.selectResumeInfo(model, num);
        return "resume/update";
    }

    // 이력서 업데이트 pro
    @PutMapping("/posts/{num}/update")
    public String updatePro(ResumeDTO Rdto, Principal principal, String careerName, @PathVariable Long num,
                            @RequestParam("jobName") List<String> jobNames,
                            @RequestParam("licenceName") List<String> licenceNames,
                            @RequestParam("equipmentName") List<String> equipmentNames,
                            @RequestParam("certificationName") List<String> certificationNames) {

        Rdto.setMemberId(principal.getName());
        resumeService.resumeUpdate(Rdto, careerName, jobNames, num, licenceNames, equipmentNames, certificationNames);

        return "redirect:/resume/posts/{num}";
    }
}
