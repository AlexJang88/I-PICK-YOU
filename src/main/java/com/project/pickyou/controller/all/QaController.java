package com.project.pickyou.controller.all;

import com.project.pickyou.dto.QaDTO;
import com.project.pickyou.service.QaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/qa/*")
@RequiredArgsConstructor
public class QaController {

    private final QaService qaService;

    // qa 리스트 가져오기, 페이징 처리
    @GetMapping("/posts")
    public String qaList(Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                         HttpSession session, Principal principal) {
        String memberId="";

        if(principal!=null) {
            memberId = principal.getName();
        }
        String sid = (String) session.getAttribute(memberId);
        // qa 리스트 가져오기
        qaService.AllPosts(model, pageNum);
        return "qa/list";
    }

    // qa 글쓰기
    @GetMapping("/posts/new")
    public String qaWrite() {
        return "qa/write";
    }

    // qa 글쓰기 pro
    @PostMapping("/posts")
    public String qaWritePro(QaDTO dto, Principal principal) {
        String memberId = principal.getName();
        dto.setMemberId(memberId);
        // qa insert
        qaService.qaInsert(dto);
        return "redirect:/qa/posts";
    }

    // qa 상세정보 가져오기
    @GetMapping("/posts/{ref}")
    public String qaInfoRef(Model model, @PathVariable int ref, HttpSession session, Principal principal) {

        String sid = principal.getName();
        model.addAttribute("sid", sid);

       // System.out.println(sid+" : ----------------------------------------------------------");
        // qa 댓글 유무
        qaService.qaReplyCount(ref, model);
        // qa 상세정보 가져오기
        qaService.qaInformation(model, ref);
        return "/qa/info";
    }

    // qa 댓글쓰기 pro
    @PostMapping("/reply/{ref}")
    public String qaReplyPro(QaDTO dto, Model model, @PathVariable int ref) {
        // qa 댓글 인서트
        qaService.qaReplyInsert(dto, ref);
        return "redirect:/qa/posts/{ref}";
    }
}
