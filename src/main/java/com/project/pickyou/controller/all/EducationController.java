package com.project.pickyou.controller.all;

import com.project.pickyou.dto.EducationDTO;
import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/educations/*")
@RequiredArgsConstructor
public class EducationController {
    private final EducationService service;

    @GetMapping("/posts")
    public String list(Model model,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,Principal principal){
            model.addAttribute("memberId",principal.getName());

            service.AllPosts(model,pageNum);
        return "education/list";
    }
   @GetMapping("/posts/{boardNum}")
   public String educationsContent(Model model,@PathVariable Long boardNum,Principal principal){
       String sid = principal.getName();
       //principal.getName();
        service.post(model,boardNum,sid,2);
        return "education/content";
   }
   //수정페이지 이동
   @GetMapping("posts/edit/{boardNum}")
   public String edit(Model model,@PathVariable Long boardNum,Principal principal){
       String sid = principal.getName();
       //principal.getName();
       service.post(model,boardNum,sid,2);
        return "education/update";
   }

   //수정
   @PutMapping("/posts")
    public String update(@RequestParam(name = "id")Long id,@RequestParam(name = "files",required = false) ArrayList<MultipartFile> files, EducationDTO dto){
        for(MultipartFile mf:files){
            if(mf.getOriginalFilename().isEmpty()){
                files = new ArrayList<>();
            }
        }
       service.update(files,dto,2);
        String url = "redirect:/educations/posts/"+id;

        return url;
   }
   //삭제
   @DeleteMapping("/posts")
   public String delete(@RequestParam(name = "id")Long id){
        System.out.println("=========deletemapping"+id);
        service.deletePost(id,2);
        return "redirect:/educations/posts";
   }
   //작성페이지이동
    @GetMapping("/posts/write/{memberId}")
    public String write(Model model,@PathVariable String memberId){
        model.addAttribute("memberId",memberId);
        return "education/write";
    }
    //작성
    @PostMapping("/posts")
    public String insertpost(ArrayList<MultipartFile> files, EducationDTO dto,Principal principal){
        //사업자 아이디 = 로그인 구현후 session 받아와서 다시 처리
        dto.setCompanyId(principal.getName());
        String content = dto.getContent();
        content = content.replace("\r\n","<br>");
        dto.setContent(content);
        service.writePost(files,dto,2);
        return "redirect:/educations/posts";
    }
    @GetMapping("/favorits/{boardNum}/{target}")
    public String checkFavoritecheck(@PathVariable Long boardNum,@PathVariable String target,Principal principal){
            String sid = principal.getName();
        //principal.getName(); 로그인 적용후 번경
        PickDTO dto = new PickDTO();
            dto.setPicker(sid);
            dto.setTarget(target);
            String url = "redirect:/educations/posts/"+boardNum;
        service.favoriteCheck(dto);
        return url;
    }

}
