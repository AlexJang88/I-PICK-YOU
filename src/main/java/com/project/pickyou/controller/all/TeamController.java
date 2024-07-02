package com.project.pickyou.controller.all;

import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.dto.TeamResumeDTO;
import com.project.pickyou.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;

    @GetMapping("/posts")
    public String list(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
        model.addAttribute("memberId","eight");

        service.AllPosts(model,pageNum);
        return "team/list";
    }
    @GetMapping("/posts/{boardNum}")
    public String teamsContent(Model model,@PathVariable Long boardNum){
        String sid = "one";
        //principal.getName();
        service.post(model,boardNum,sid);
        return "team/content";
    }
    //수정페이지 이동
    @GetMapping("posts/edit/{boardNum}")
    public String edit(Model model,@PathVariable Long boardNum){
        String sid = "one";
        //principal.getName();
        service.post(model,boardNum,sid);
        return "team/update";
    }

    //수정
    @PutMapping("/posts")
    public String update(@RequestParam(name = "id")Long id, MultipartFile profileimg,ArrayList<MultipartFile> files, TeamResumeDTO dto){
        service.update(profileimg,files,dto);
        String url = "redirect:/teams/posts/"+id;

        return url;
    }
    //삭제
    @PatchMapping("/posts")
    public String exposure(TeamResumeDTO dto){
        System.out.println("=========deletemapping"+dto.getStatus());
        service.exposure(dto);
        String url = "redirect:/teams/posts/"+dto.getId();
        return url;
    }
    //작성페이지이동
    @GetMapping("/posts/write/{memberId}")
    public String write(Model model,@PathVariable String memberId){
        model.addAttribute("memberId",memberId);
        return "team/write";
    }
    //작성
    @PostMapping("/posts")
    public String insertpost(ArrayList<MultipartFile> files, TeamResumeDTO dto,MultipartFile profileimg){
        //사업자 아이디 = 로그인 구현후 session 받아와서 다시 처리
        dto.setMemberId("seven");
        String content = dto.getIntroduction();
        content = content.replace("\r\n","<br>");
        dto.setIntroduction(content);
        service.writePost(profileimg,files,dto);
        return "redirect:/teams/posts";
    }
    @GetMapping("/favorits/{boardNum}/{target}")
    public String checkFavoritecheck(@PathVariable Long boardNum,@PathVariable String target){
        String sid = "one";
        //principal.getName(); 로그인 적용후 번경
        PickDTO dto = new PickDTO();
        dto.setPicker(sid);
        dto.setTarget(target);
        String url = "redirect:/teams/posts/"+boardNum;
        service.favoriteCheck(dto);
        return url;
    }
}
