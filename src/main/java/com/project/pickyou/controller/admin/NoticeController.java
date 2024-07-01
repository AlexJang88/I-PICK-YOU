package com.project.pickyou.controller.admin;

import com.project.pickyou.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice/*")
public class NoticeController {

    public final NoticeService noticeService;

    // 공지사항 리스트 가져오기
    @GetMapping("/posts")
    public String noticeList(Model model) {
        noticeService.noticeList(model);
        return "notice/list";
    }

    // 공지사항 글쓰기
    @GetMapping("postsWrite")
    public String noticeWrite() {
        return "notice/write";
    }
}
