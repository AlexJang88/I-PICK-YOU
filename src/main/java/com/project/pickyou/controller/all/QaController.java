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
    @GetMapping("information")
    public String qaInformation() {
        return "/qa/information";
    }

    @GetMapping("test/{ref}")
    public String test(Model model, @PathVariable int ref) {
        qaService.qaInformation(model, ref);
        return "qa/test";
    }



    @GetMapping("test")
    public String test2() {
        return "qa/test";
    }
}
