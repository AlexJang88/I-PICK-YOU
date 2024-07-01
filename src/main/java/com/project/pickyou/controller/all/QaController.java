package com.project.pickyou.controller.all;

import com.project.pickyou.dto.QaDTO;
import com.project.pickyou.service.QaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/qa/*")
@RequiredArgsConstructor
public class QaController {

    private final QaService qaService;

    // qa 게시판
    @GetMapping("posts")
    public String qaList(Model model) {
        // qa 리스트 가져오기
        qaService.qaList(model);
        return "qa/list";
    }

    // qa 글쓰기
    @GetMapping("postsWrite")
    public String qaWrite() {
        return "qa/write";
    }

    // qa 글쓰기 pro
    @PostMapping("posts")
    public String qaWritePro(QaDTO dto) {
        // qa insert
        qaService.qaInsert(dto);
        return "redirect:/qa/posts";
    }

    // qa 상세정보 가져오기
    @GetMapping("info/{ref}")
    public String qaInfoRef(Model model, @PathVariable int ref) {
        // qa 댓글 유무
        qaService.qaReplyCount(ref, model);
        // qa 상세정보 가져오기
        qaService.qaInformation(model, ref);
        return "/qa/info";
    }

    // qa 댓글쓰기
    @GetMapping("reply/{ref}")
    public String qaReply(Model model, @PathVariable int ref) {
        // qa 상세정보 가져오기
        qaService.qaInformation(model, ref);
        return "/qa/reply";
    }

    // qa 댓글쓰기 pro
    @PostMapping("reply/{ref}")
    public String qaReplyPro(QaDTO dto, Model model, @PathVariable int ref) {
        // qa 댓글 인서트
        qaService.qaReplyInsert(dto, ref);
        return "redirect:/qa/posts";
    }
}
