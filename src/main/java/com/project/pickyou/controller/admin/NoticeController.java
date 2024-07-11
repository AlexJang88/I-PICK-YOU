package com.project.pickyou.controller.admin;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.NoticeDTO;
import com.project.pickyou.entity.NoticeEntity;
import com.project.pickyou.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice/*")
public class NoticeController {

    public final NoticeService noticeService;

    // 공지사항 리스트 가져오기
    @GetMapping("/posts")
    public String noticeList(Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
        noticeService.AllPosts(model, pageNum);
        return "notice/list";
    }

    // 공지사항 글쓰기
    @GetMapping("posts/new")
    public String noticeWrite() {
        return "notice/write";
    }

    // 공지사항 글쓰기 pro
    @PostMapping("posts")
    public String noticeWritePro(NoticeDTO dto, ImageDTO imageDTO, Principal principal,
                                 @RequestParam("files") MultipartFile[] files) {

        String memberId = principal.getName();
        dto.setMemberId(memberId);
        // 공지사항 정보 저장 및 인서트
        NoticeEntity saveNotice = noticeService.noticeInsert(dto);

        // 위에서 이미지 파일에 등록된 훈련소 번호의 숫자를 가져옴
        Long noticeId = saveNotice.getId();

        imageDTO.setBoardNum(noticeId);
        // 공지사항 이미지 인서트
        noticeService.saveImage(imageDTO, files);

        return "redirect:/notice/posts";
    }

    // 공지사항 상세정보
    @GetMapping("/posts/{boardNum}")
    public String noticeInfo(@PathVariable Long boardNum, Model model, HttpSession session, Principal principal) {
        int boardType = 1;
        noticeService.noticeImage(boardNum, boardType, model);

        noticeService.noticeInfo(boardNum, model);

        String memberId = principal.getName();
        // 공지사항 조회수 증가
        String sid = (String)session.getAttribute(memberId);
        if(session.getAttribute(sid+"1_"+boardNum)==null){
            noticeService.noticeCnt(boardNum, model);
            session.setAttribute(sid+"1_"+boardNum,"true");
        }
        return "notice/info";
    }

    // 공지사항 글 삭제
    @DeleteMapping("/posts/{boardNum}")
    public String deleteNotice(@PathVariable Long boardNum) {
        noticeService.noticeDelete(boardNum);
        return "redirect:/notice/posts";
    }

    // 공지사항 글 수정
    @GetMapping("/posts/{boardNum}/edit")
    public String noticeUpdate(Model model, @PathVariable Long boardNum) {
        noticeService.noticeUpdate(model, boardNum);
        return "notice/update";
    }

    // 공지사항 글 수정 pro
    @PutMapping("/posts/{id}")
    public String noticeUpdatePro(@PathVariable Long id, ArrayList<MultipartFile> files, NoticeDTO dto) {
        noticeService.update(files, dto);
        return "redirect:/notice/posts/"+id;
    }


    @GetMapping()
    public String IntroductionUs(){  //사이트 소개하기

        return "/notice/IntroductionUs";
    }


}
