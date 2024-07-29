package com.project.pickyou.controller.all;

import com.project.pickyou.dto.EducationDTO;
import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.service.EducationService;
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
@RequestMapping("/educations/*")
@RequiredArgsConstructor
public class EducationController {
    private final EducationService service;
    private int type=2;

    @GetMapping("/posts")
    public String list(Model model,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,Principal principal){
        int mem = 0;
        if(principal!=null){
        model.addAttribute("memberId",principal.getName());
        model.addAttribute("id",principal.getName());
        }
            mem=service.authCheck(principal);
            service.AllPosts(model,pageNum);
            model.addAttribute("auth",mem);
            return "education/list";

    }
   @GetMapping("/posts/{boardNum}")
   public String educationsContent(Model model, HttpSession session, @PathVariable Long boardNum, Principal principal, HttpServletRequest request){
       String sid="";
       String ip=request.getHeader("X-FORWARDED-FOR");
       if(ip==null){
           ip=request.getRemoteAddr();
       }
       if(principal!=null){
           sid= principal.getName();
           model.addAttribute("to",sid);
           model.addAttribute("id",principal.getName());
       }else{
           sid = ip;
       }
       if(session.getAttribute(sid+"_"+type+"_"+boardNum)==null){
           service.updateReadCount(boardNum);
           session.setAttribute(sid+"_"+type+"_"+boardNum,"true");
       }


       //principal.getName();
        service.post(model,boardNum,sid,2);

        return "education/content";
   }
   //수정페이지 이동
   @PreAuthorize("hasRole('ROLE_COMPANY')")
   @GetMapping("posts/{boardNum}/edit")
   public String edit(Model model,@PathVariable Long boardNum,Principal principal){
       String sid="";
       String url="redirect:/";
       if(principal!=null){
           sid= principal.getName();
           model.addAttribute("id",principal.getName());
            if(service.authCheck(boardNum,sid,type)){
                url="education/update";
                service.post(model,boardNum,sid,2);
            }
       }
        return url;
   }

   //수정
   @PreAuthorize("hasRole('ROLE_COMPANY')")
   @PutMapping("/posts/{id}")
    public String update(@PathVariable(name = "id")Long id,@RequestParam(name = "files",required = false) ArrayList<MultipartFile> files, EducationDTO dto){
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
   @PreAuthorize("hasRole('ROLE_COMPANY')")
   @DeleteMapping("/posts/{id}")
   public String delete(@PathVariable(name = "id")Long id){
        service.deletePost(id,2);
        return "redirect:/educations/posts";
   }
   //작성페이지이동
   @PreAuthorize("hasRole('ROLE_COMPANY')")
    @GetMapping("/posts/new")
    public String write(Model model,Principal principal){
        String sid="";
        String url = "education/write";
        if(principal!=null){
        sid = principal.getName();
            model.addAttribute("id",principal.getName());
        }else if(principal==null){
            url="redirect:/educations/posts";
        }
        model.addAttribute("memberId",sid);
        return url;
    }
    //작성
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @PostMapping("/posts")
    public String insertpost(ArrayList<MultipartFile> files, EducationDTO dto,Principal principal){
        //사업자 아이디 = 로그인 구현후 session 받아와서 다시 처리
        if(principal!=null){dto.setCompanyId(principal.getName());}
        String content = dto.getContent();
        content = content.replace("\r\n","<br>");
        dto.setContent(content);
        service.writePost(files,dto,2);
        return "redirect:/educations/posts";
    }
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/favorits/{boardNum}/{target}")
    public String checkFavoritecheck(@PathVariable Long boardNum,@PathVariable String target,Principal principal){
        String sid="";
        if(principal!=null){
            sid= principal.getName();
            if(!sid.equals(target)){
                PickDTO dto = new PickDTO();
                dto.setPicker(sid);
                dto.setTarget(target);
                service.favoriteCheck(dto);
            }
        }
        //principal.getName(); 로그인 적용후 번경

        String url = "redirect:/educations/posts/"+boardNum;
        return url;
    }


}
