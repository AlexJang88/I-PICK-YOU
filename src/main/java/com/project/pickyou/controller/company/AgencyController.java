package com.project.pickyou.controller.company;


import com.project.pickyou.dto.AgencyDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.TrainningDTO;
import com.project.pickyou.entity.AgencyEntity;
import com.project.pickyou.service.AgencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collection;

@Controller
@RequestMapping("/agency/*")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyService agencyService;

    private int type=7;

    //테스트
    @GetMapping("/test")
    public String agencymain(){

        return "agency/ad";
    }



    //소개소 메인페이지
    @GetMapping("/posts")
    public String agencymain(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, Principal pc){
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }
        agencyService.agencymain(model, pageNum);
        return "agency/agencymain";
    }

    //소개소 상세페이지
    @GetMapping("/posts/{agencynum}")
    public String agencyDetails(Model model, @PathVariable Long agencynum, HttpSession session, Principal principal, HttpServletRequest request){
        if(principal != null){
            model.addAttribute("id",principal.getName()) ;
        }

        String sid="";
        String ip=request.getHeader("X-FORWARDED-FOR");
        if(ip==null){
            ip=request.getRemoteAddr();
        }

        if(principal != null){  //로그인 되어잇을때 조회수 올리기

           sid = principal.getName();
            model.addAttribute("pid", sid);
            if(session.getAttribute(sid+"_"+type+"_"+agencynum)==null){
                agencyService.agencyCount(agencynum);
                session.setAttribute(sid+"_"+type+"_"+agencynum,"true");
            }

        }else{   //로그인안되어있을때 조회수 올리기
            sid=ip;
            if(session.getAttribute(sid+"_"+type+"_"+agencynum)==null){
                agencyService.agencyCount(agencynum);
                session.setAttribute(sid+"_"+type+"_"+agencynum,"true");
            }
        }



        agencyService.agencyDetails(model, agencynum);
        return "agency/agencyDetails";
    }




    //소개소 추가하기
    @GetMapping("/posts/new")
    public String agencyInput(Principal pc, Model model){
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }

        return "agency/agencyInput";
    }

    @PostMapping("/posts")
    public String agencyPro(AgencyDTO agencyDTO, ImageDTO imageDTO, @RequestParam("files")MultipartFile [] files, Principal principal){


        String companyId = principal.getName();  //세션아이디설정
        agencyDTO.setCompanyId(companyId); // 디티오넣기


        agencyService.saveImage(agencyDTO, files);//값 저장하기
        return "redirect:/agency/posts";
    }

    //소개소내용삭제
    @DeleteMapping("/posts/{agencynum}")
    public String agencyDelete(@PathVariable Long agencynum) {

        agencyService.deleteAgencyImg(agencynum);  //소개소 및 이미지 지우기

        return "redirect:/agency/posts";
    }


    @GetMapping("/posts/{agencynum}/edit")
    public String agencyUpdate(Model model,@PathVariable Long agencynum, Principal pc){
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }
        agencyService.agencyDetails(model,agencynum);
        return "agency/agencyUpdate";
    }

    @PatchMapping("/posts/{agencynum}")
    public String agencyUpdates(@PathVariable Long agencynum, AgencyDTO agencyDTO, @RequestParam("files") MultipartFile[] files){

        agencyService.agencyUpdate(agencynum, agencyDTO, files);
        return "redirect:/agency/posts/"+agencynum;
    }






}
