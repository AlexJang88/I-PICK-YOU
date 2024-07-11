package com.project.pickyou.controller.company;


import com.project.pickyou.dto.AgencyDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.TrainningDTO;
import com.project.pickyou.entity.AgencyEntity;
import com.project.pickyou.service.AgencyService;
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


    //소개소 메인페이지
    @GetMapping("/posts")
    public String agencymain(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum){

        agencyService.agencymain(model, pageNum);
        return "/agency/agencymain";
    }

    //소개소 상세페이지
    @GetMapping("/posts/{agencynum}")
    public String agencyDetails(Model model, @PathVariable Long agencynum, Principal principal){
        if(principal != null){

            String pid = principal.getName();
            model.addAttribute("pid", pid);
        }

        agencyService.agencyDetails(model, agencynum);
        return "/agency/agencyDetails";
    }




    //소개소 추가하기
    @GetMapping("/posts/new")
    public String agencyInput(){


        return "/agency/agencyInput";
    }

    @PostMapping("/posts")
    public String agencyPro(AgencyDTO agencyDTO, ImageDTO imageDTO, @RequestParam("files")MultipartFile [] files, Principal principal){

        String companyId = principal.getName();  //세션아이디설정
        agencyDTO.setCompanyId(companyId); // 디티오넣기

        AgencyEntity saveAgency = agencyService.saveAgency(agencyDTO);  //값 저장하기
        Long agencyId =  saveAgency.getId(); // 이미지 넣기 위해 번호 갑 가져오기

        imageDTO.setBoardNum(agencyId);
        agencyService.saveImage(imageDTO, files);
        return "redirect:/agency/posts";
    }

    //소개소내용삭제
    @DeleteMapping("/posts/{agencynum}")
    public String agencyDelete(@PathVariable Long agencynum) {

        agencyService.deleteAgencyImg(agencynum);  //소개소 이미지 지우기
        agencyService.deleteAgencyNum(agencynum);  //소개소 한개 지우기

        return "redirect:/agency/posts";
    }


    @GetMapping("/posts/{agencynum}/edit")
    public String agencyUpdate(Model model,@PathVariable Long agencynum){

        agencyService.agencyDetails(model,agencynum);

        return "/agency/agencyUpdate";
    }

    @PatchMapping("/posts/{agencynum}")
    public String agencyUpdates(@PathVariable Long agencynum, AgencyDTO agencyDTO, @RequestParam("files") MultipartFile[] files){

        agencyService.agencyUpdate(agencynum, agencyDTO, files);
        return "redirect:/agency/posts/"+agencynum;
    }






}
