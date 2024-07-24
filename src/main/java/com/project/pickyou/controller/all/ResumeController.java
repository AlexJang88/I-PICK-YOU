package com.project.pickyou.controller.all;

import com.project.pickyou.dto.ConfirmDTO;
import com.project.pickyou.dto.ResumeDTO;
import com.project.pickyou.entity.ResumeEntity;
import com.project.pickyou.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume/*")
public class ResumeController {

    private final ResumeService resumeService;

    // 이력서 리스트
    @GetMapping("/posts")
    public String list(Principal principal, Model model) {
        String sid = "";

        if(principal!=null) {
            sid = principal.getName();
            model.addAttribute("id", principal.getName());
        }

        resumeService.selectResume(model, sid);
        return "resume/list";
    }

    // 이력서 작성
    @GetMapping("/posts/new")
    public String write(Principal principal, Model model) {
        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@
        return "resume/write";
    }

    // 이력서 인서트
    @PostMapping("/posts")
    public String writePro(ResumeDTO Rdto, Principal principal, String careerName,
                           @RequestParam("jobName") List<String> jobNames,
                           @RequestParam("licenceName") List<String> licenceNames,
                           @RequestParam("equipmentName") List<String> equipmentNames,
                           @RequestParam("certificationName") List<String> certificationNames,
                           @RequestParam("address") String address) {

        // 이력서 인서트
        Rdto.setMemberId(principal.getName());
        Rdto.setLocal(address);
        resumeService.resumeInsert(Rdto, careerName, jobNames, licenceNames, equipmentNames, certificationNames);

        return "redirect:/resume/posts";
    }

    // 이력서 상세정보
    @GetMapping("/posts/{num}")
    public String info(@PathVariable Long num, Model model, Principal principal) {

        String sessionId="";

        if(principal!=null) {
            sessionId = principal.getName();
            model.addAttribute("id", principal.getName());
        }

        model.addAttribute("sessionId", sessionId);


        resumeService.selectResumeInfo(model, num);

        return "resume/info";
    }

    // 지원자 목록에서 이력서 볼 때
    @GetMapping("/posts/{memberId}/info")
    public String infoGO(@PathVariable String memberId, Model model, RedirectAttributes redirectAttributes) {
        Optional<ResumeEntity> optional = resumeService.findBymemberId(memberId, 1);
        if (optional.isPresent()) {
            ResumeEntity entity = optional.get();
            redirectAttributes.addAttribute("num", entity.getId());
        }
        return "redirect:/resume/posts/{num}";
    }


    // 이력서 삭제
    @DeleteMapping("/posts/{num}")
    public String delete(@PathVariable Long num) {
        resumeService.deleteResume(num);
        return "redirect:/resume/posts";
    }

    // 이력서 업데이트
    @GetMapping("/posts/{num}/edit")
    public String update(@PathVariable Long num, Model model, Principal principal) {
        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        resumeService.selectResumeInfo(model, num);
        return "resume/update";
    }

    // 이력서 업데이트 pro
    @PutMapping("/posts/{num}/update")
    public String updatePro(ResumeDTO Rdto, Principal principal, String careerName, @PathVariable Long num,
                            @RequestParam("jobName") List<String> jobNames,
                            @RequestParam("licenceName") List<String> licenceNames,
                            @RequestParam("equipmentName") List<String> equipmentNames,
                            @RequestParam("certificationName") List<String> certificationNames,
                            @RequestParam("address") String address) {

        Rdto.setMemberId(principal.getName());
        Rdto.setLocal(address);
        resumeService.resumeUpdate(Rdto, careerName, jobNames, num, licenceNames, equipmentNames, certificationNames);

        return "redirect:/resume/posts/{num}";
    }

    // 인재 정보 리스트
    @GetMapping("/all/posts")
    public String allPosts(Model model, Principal principal,
                           @RequestParam(value = "pageNum",defaultValue = "1") int pageNum) {
        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@
        resumeService.AllPosts(model, pageNum);
        return "resume/allList";
    }

    // 인재공고보고 사업자가 채용
    @PostMapping("/posts/confirm")
    public String confirmInsert(@RequestParam("memberId") String memberId,
                                @RequestParam("num") Long num, Principal principal) {

        ConfirmDTO dto = new ConfirmDTO();
        dto.setMemberId(memberId);
        dto.setCompanyId(principal.getName());
        dto.setApply(2);
        resumeService.confirmInsert(dto);

        return "redirect:/resume/posts/"+num;
    }

}
