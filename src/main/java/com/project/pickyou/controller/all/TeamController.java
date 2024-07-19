package com.project.pickyou.controller.all;

import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.dto.TeamResumeDTO;
import com.project.pickyou.service.EducationService;
import com.project.pickyou.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;
    private final EducationService educationService;

    @GetMapping("/posts")
    public String list(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,Principal principal){
        int mem=0;
        if(principal!=null){
            model.addAttribute("memberId",principal.getName());
            mem= educationService.authCheck(principal);
        }
        model.addAttribute("auth",mem);
        service.AllPosts(model,pageNum);
        return "team/list";
    }
    @GetMapping("/posts/my")
    public String myList(Model model,Principal principal){
        String url ="redirect:/educations/posts";
        String sid="";
        if(principal!=null){
            sid= principal.getName();
            url="team/myContent";
            service.MyPosts(model,sid,5);
        }
        return url;
    }
    @GetMapping("/posts/{boardNum}")
    public String teamsContent(Model model, @PathVariable Long boardNum, Principal principal){
        String sid ="";
        if(principal!=null) {
             sid = principal.getName();
        }
        model.addAttribute("to",sid);
        service.post(model,boardNum,sid,5);

        return "team/content";
    }
    //수정페이지 이동
    @GetMapping("posts/edit/{boardNum}")
    public String edit(Model model,@PathVariable Long boardNum,Principal principal){
        String sid = "";
        if(principal!=null) {
         sid=   principal.getName();
        }
        //principal.getName();
        service.post(model,boardNum,sid,5);
        return "team/update";
    }
    //수정
    @PutMapping("/posts")
    public String update(@RequestParam(name = "id")Long id, MultipartFile profileimg,ArrayList<MultipartFile> files, TeamResumeDTO dto){
        for(MultipartFile mf:files){
            if(mf.getOriginalFilename().isEmpty()){
                files = new ArrayList<>();
            }
        }
         service.update(profileimg,files,dto,5);
        String url = "redirect:/teams/posts/"+id;
        return url;
    }
    //비공개
    @PatchMapping("/posts")
    public String exposure(TeamResumeDTO dto){
        service.exposure(dto);
        String url = "redirect:/teams/posts/"+dto.getId();
        return url;
    }
    //작성페이지이동
    @GetMapping("/posts/write/{memberId}")
    public String write(Model model,@PathVariable String memberId,Principal principal){
        boolean check=false;
        String url="team/write";
        model.addAttribute("memberId",memberId);
        if(principal!=null){
            check=service.checkMyPost(principal.getName());
        }
        if(!check){
            url="redirect:/teams/posts/my";
        }
        return url;
    }
    //작성
    @PostMapping("/posts")
    public String insertpost(ArrayList<MultipartFile> files, TeamResumeDTO dto,MultipartFile profileimg,Principal principal){
        //사업자 아이디 = 로그인 구현후 session 받아와서 다시 처리
        if(principal!=null){
        dto.setMemberId(principal.getName());}
        String content = dto.getIntroduction();
        content = content.replace("\r\n","<br>");
        dto.setIntroduction(content);
        service.writePost(profileimg,files,dto,5);
        return "redirect:/teams/posts";
    }
    @GetMapping("/favorits/{boardNum}/{target}")
    public String checkFavorite(@PathVariable Long boardNum,@PathVariable String target,Principal principal){
        String sid = "";
        if(principal!=null) {
           sid= principal.getName();
        }
        //principal.getName(); 로그인 적용후 번경
        PickDTO dto = new PickDTO();
        dto.setPicker(sid);
        dto.setTarget(target);
        String url = "redirect:/teams/posts/"+boardNum;
        service.favoriteCheck(dto);
        return url;
    }
}
