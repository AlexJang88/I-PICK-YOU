package com.project.pickyou.controller.admin;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.NoticeDTO;
import com.project.pickyou.entity.NoticeEntity;
import com.project.pickyou.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // 공지사항 글번호
    int type = 1;

    // 공지사항 리스트 가져오기
    @GetMapping("/posts")
    public String noticeList(Model model, Principal principal,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {

        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        noticeService.AllPosts(model, pageNum);
        return "notice/list";
    }

    // 공지사항 글쓰기
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("posts/new")
    public String noticeWrite(Principal principal, Model model) {
        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@
        return "notice/write";
    }

    // 공지사항 글쓰기 pro
    @PostMapping("posts")
    public String noticeWritePro(NoticeDTO dto, Principal principal,
                                 ArrayList<MultipartFile> files) {

        if (principal != null) {
            dto.setMemberId(principal.getName());
        }
        String content = dto.getContent();
        content = content.replace("\r\n","<br>");
        dto.setContent(content);

        // 공지사항 이미지 인서트
        noticeService.saveImage(dto, files);

        return "redirect:/notice/posts";
    }

    // 공지사항 상세정보
    @GetMapping("/posts/{boardNum}")
    public String noticeInfo(@PathVariable Long boardNum, Model model, HttpSession session, Principal principal, HttpServletRequest request) {
        int boardType = 1;
        noticeService.noticeImage(boardNum, boardType, model);

        noticeService.noticeInfo(boardNum, model);

        String sid="";
        String ip=request.getHeader("X-FORWARDED-FOR");
        if(ip==null){
            ip=request.getRemoteAddr();
        }

        if(principal != null){  //로그인 되어잇을때 조회수 올리기
            sid = principal.getName();
            model.addAttribute("id", principal.getName());

            if(session.getAttribute(sid+"_"+type+"_"+boardNum)==null){
                noticeService.noticeCnt(boardNum, model);
                session.setAttribute(sid+"_"+type+"_"+boardNum,"true");
            }}else{   //로그인안되어있을때 조회수 올리기
            sid=ip;
            if(session.getAttribute(sid+"_"+type+"_"+boardNum)==null){
                noticeService.noticeCnt(boardNum, model);
                session.setAttribute(sid+"_"+type+"_"+boardNum,"true");
            }
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/posts/{boardNum}/edit")
    public String noticeUpdate(Model model, @PathVariable Long boardNum, Principal principal) {
        // @@
        if(principal!=null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        noticeService.noticeUpdate(model, boardNum);
        return "notice/update";
    }

    // 공지사항 글 수정 pro
    @PutMapping("/posts/{id}")
    public String noticeUpdatePro(@PathVariable Long id, NoticeDTO dto,
                                  @RequestParam(name = "files",required = false) ArrayList<MultipartFile> files) {

        for(MultipartFile mf:files){
            if(mf.getOriginalFilename().isEmpty()){
                files = new ArrayList<>();
            }
        }
        noticeService.update(files, dto);

        return "redirect:/notice/posts/"+id;
    }

    @GetMapping()
    public String IntroductionUs(Principal pc, Model model){  //사이트 소개하기
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }

        return "notice/IntroductionUs";
    }
}
