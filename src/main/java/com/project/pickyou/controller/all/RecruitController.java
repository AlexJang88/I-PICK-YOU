package com.project.pickyou.controller.all;

import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.dto.RecruitDTO;
import com.project.pickyou.dto.RecruitDetailDTO;
import com.project.pickyou.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Date;

@Controller
@RequestMapping ("/recruit/*")
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService service;

    @GetMapping("/posts")
    public String list(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
        model.addAttribute("memberId","four");

        service.AllPosts(model,pageNum);
        return "recruit/list";
    }
    @GetMapping("/posts/{boardNum}")
    public String educationsContent(Model model,@PathVariable Long boardNum){
        String sid = "one";
        //principal.getName();
        service.post(model,boardNum,sid);
        return "recruit/content";
    }
    //수정페이지 이동
    @GetMapping("posts/edit/{boardNum}")
    public String edit(Model model,@PathVariable Long boardNum){
        String sid = "one";
        //principal.getName();
        service.post(model,boardNum,sid);
        return "recruit/update";
    }
    //수정
    @PutMapping("/posts")
    public String update(@RequestParam(name = "id")Long id, ArrayList<MultipartFile> files, RecruitDTO dto){
        service.update(files,dto,6);
        String url = "redirect:/recruit/posts/"+id;
        return url;
    }
    //삭제
    @DeleteMapping("/posts")
    public String delete(@RequestParam(name = "id")Long id){
        service.deletePost(id);
        return "redirect:/recruit/posts";
    }
    //작성페이지이동
    @GetMapping("/posts/write/{memberId}")
    public String write(Model model,@PathVariable String memberId){
        model.addAttribute("memberId",memberId);
        return "recruit/write";
    }
    //작성
    @PostMapping("/posts")
    public String insertpost(ArrayList<MultipartFile> files, RecruitDTO rdto, RecruitDetailDTO rddto,
                             @RequestParam("startDateParam") @DateTimeFormat(pattern = "yyyy-MM-dd")Date startDateParam,
                             @RequestParam("endDateParam") @DateTimeFormat(pattern = "yyyy-MM-dd")Date endDateParam
    ){
        int check=0;
        check = rdto.getStatus();
        String url="redirect:/recruit/posts";
        if(check==2){
            //결제 페이지로 이동
            url="redirect:/recruit/posts";
        }
        //사업자 아이디 = 로그인 구현후 session 받아와서 다시 처리
        rdto.setMemberId("four");


        check = rdto.getStatus();
        rdto.setStartDate(startDateParam);
        rdto.setEndDate(endDateParam);
        rdto.setMemberId("four");
        String content = rdto.getContent();
        content = content.replace("\r\n","<br>");
        rdto.setContent(content);
        service.writePost(files,rdto,rddto,6);

        return url;
    }

    @GetMapping("/favorits/{boardNum}/{target}")
    public String checkFavoritecheck(@PathVariable Long boardNum,@PathVariable String target){
        String sid = "one";
        //principal.getName(); 로그인 적용후 번경
        PickDTO dto = new PickDTO();
        dto.setPicker(sid);
        dto.setTarget(target);
        String url = "redirect:/recruit/posts/"+boardNum;
        service.favoriteCheck(dto);
        return url;
    }
    @PostMapping("/apply/{boardNum}")
    public String apply(@PathVariable Long boardNum){
        //로그인 처리후 세션으로 변경
        String sid = "five";
        String url="redirect:/recruit/posts/"+boardNum;
        if(service.recruit(boardNum,sid)==0){
            //추후에 이력서 등록페이지 만들어지면 url 바꿔야함
            url="redirect:/recruit/posts";
        }
        return url;
    }
}
