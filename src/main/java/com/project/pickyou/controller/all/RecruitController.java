package com.project.pickyou.controller.all;

import com.project.pickyou.dto.ContractDTO;
import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.dto.RecruitDTO;
import com.project.pickyou.dto.RecruitDetailDTO;
import com.project.pickyou.service.EducationService;
import com.project.pickyou.service.RecruitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping ("/recruit/*")
@RequiredArgsConstructor
public class RecruitController {

    private int type=6;

    private final RecruitService service;
    private final EducationService educationService;

    @GetMapping("/posts")
    public String list(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, Principal principal,@RequestParam(value = "type",defaultValue = "1") int type){
        int mem=0;
        if(principal!=null) {
            model.addAttribute("memberId", principal.getName());
           mem=educationService.authCheck(principal);
            model.addAttribute("id",principal.getName());
        }
        model.addAttribute("auth",mem);
        model.addAttribute("type",type);
        service.AllPosts(model,pageNum,type);
        return "recruit/list";
    }
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @GetMapping("/posts/my")
    public  String mylist(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, Principal principal){
        String memberId="";
        if(principal!=null) {
            memberId= principal.getName();
            model.addAttribute("memberId", principal.getName());
            model.addAttribute("id",principal.getName());
        }
        service.myPosts(model,pageNum,memberId);
        return "recruit/myList";
    }
    @GetMapping("/posts/{boardNum}")
    public String educationsContent(HttpSession session, Model model, @PathVariable Long boardNum, Principal principal, HttpServletRequest request){
        String sid="";
        String ip=request.getHeader("X-FORWARDED-FOR");
        if(ip==null){
            ip=request.getRemoteAddr();
        }
        if(principal!=null){
            sid= principal.getName();
            model.addAttribute("id",principal.getName());

        }else{
            sid = ip;
        }
        if(session.getAttribute(sid+"_"+type+"_"+boardNum)==null){
            service.updateReadCount(boardNum);
            session.setAttribute(sid+"_"+type+"_"+boardNum,"true");
        }
        service.post(model,boardNum,sid,type);
        return "recruit/content";
    }
    //수정페이지 이동
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @GetMapping("posts/edit/{boardNum}")
    public String edit(Model model,@PathVariable Long boardNum,Principal principal){
        String sid = "";
        String url="redirect:/";
        if(principal!=null) {
            sid=principal.getName();
            model.addAttribute("id",principal.getName());
            if(educationService.authCheck(boardNum,sid,type)){
                service.post(model,boardNum,sid,type);
                url="recruit/update";
            }
        }
        //principal.getName();

        return url;
    }
    //수정
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @PutMapping("/posts")
    public String update(@RequestParam(name = "id")Long id, ArrayList<MultipartFile> files, RecruitDTO rdto,RecruitDetailDTO rddto
                        ,@RequestParam("startDateParam") @DateTimeFormat(pattern = "yyyy-MM-dd")Date startDateParam,
                         @RequestParam("endDateParam") @DateTimeFormat(pattern = "yyyy-MM-dd")Date endDateParam
                         ){
        rdto.setStartDate(startDateParam);
        rdto.setEndDate(endDateParam);
        service.update(files,rdto,rddto,6);
        String url = "redirect:/recruit/posts/"+id;
        return url;
    }
    //삭제
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @DeleteMapping("/posts")
    public String delete(@RequestParam(name = "id")Long id){
        service.deletePost(id,6);
        return "redirect:/recruit/posts";
    }
    //작성페이지이동
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @GetMapping("/posts/new")
    public String write(Model model,Principal principal){
        if(principal!=null) {
            model.addAttribute("memberId", principal.getName());
            model.addAttribute("id",principal.getName());
        }
        return "recruit/write";
    }
    //작성
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @PostMapping("/posts")
    public String insertpost(ArrayList<MultipartFile> files, RecruitDTO rdto, RecruitDetailDTO rddto,
                             @RequestParam("startDateParam") @DateTimeFormat(pattern = "yyyy-MM-dd")Date startDateParam,
                             @RequestParam("endDateParam") @DateTimeFormat(pattern = "yyyy-MM-dd")Date endDateParam,Principal principal
    ){
        String sid="";
        int check=0;
        check = rdto.getStatus();
        String url="redirect:/recruit/posts";
        if(principal!=null) {
            rdto.setMemberId(principal.getName());
            sid= principal.getName();
        }
        if(check==2){
            //결제 페이지로 이동
            url="redirect:/payProcess/Pay/"+sid;
        }
        //사업자 아이디 = 로그인 구현후 session 받아와서 다시 처리

        check = rdto.getStatus();
        rdto.setStartDate(startDateParam);
        rdto.setEndDate(endDateParam);
        String content = rdto.getContent();
        content = content.replace("\r\n","<br>");
        rdto.setContent(content);
        service.writePost(files,rdto,rddto,6);

        return url;
    }

    @GetMapping("/favorits/{boardNum}/{target}")
    public String checkFavoritecheck(@PathVariable Long boardNum,@PathVariable String target,Principal principal){
        String sid = "";
        String url="redirect:/";
        if(principal!=null) {
            sid=principal.getName();
            if(!target.equals(sid)){
                PickDTO dto = new PickDTO();
                dto.setPicker(sid);
                dto.setTarget(target);
                url = "redirect:/recruit/posts/"+boardNum;
                service.favoriteCheck(dto);
            }
        }
        //principal.getName(); 로그인 적용후 번경

        return url;
    }
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/apply/{boardNum}")
    public String apply(@PathVariable Long boardNum,Principal principal){
        int check=0;
        //로그인 처리후 세션으로 변경
        String sid = "";
        if(principal!=null) {
            principal.getName();
        }
        String url="redirect:/recruit/posts/"+boardNum;
        check=service.recruit(boardNum,sid);

        if(check==0){
            //추후에 이력서 등록페이지 만들어지면 url 바꿔야함
            url="redirect:/recruit/posts";
        }
        return url;
    }
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_USER')")
    @GetMapping("/contract/{memberId}/{companyId}/{recruitId}")
    public String contract(Model model,Principal principal,@PathVariable String memberId,@PathVariable String companyId,@RequestParam int type,@PathVariable Long recruitId){
        String name ="";
        String url="redirect:/";
        //memberId=사용자 / principal=사업자
        if(principal!=null) {
             name=principal.getName();
            model.addAttribute("id",principal.getName());
            if(name.equals(companyId)||name.equals(memberId)) {
                if(type==1){
                    url="recruit/contractForm";
                    service.userInfo(model,memberId,name,recruitId,1);
                }
                else if(type==2){
                    service.basicContract(memberId,name,1,recruitId);
                    url="redirect:/";
                }
                if (type == 3) {
                    url = "recruit/contractForm";
                    service.userInfo(model, memberId, name, recruitId, 3);
                } else if (type == 4) {
                    url = "redirect:/recruit/posts";
                    service.basicContract(memberId, name, 4, recruitId);
                }
            }
        }
        return url;
    }
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_USER')")
    @PostMapping("/contract/company/{stateId}")
    public String signature(@ModelAttribute ContractDTO dto,@PathVariable Long stateId,@RequestParam int applyType){
        String url="";

         Long id=service.contract(dto,stateId,applyType);
        url="redirect:/recruit/contract/"+id;
        return url;
    }
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_USER')")
    @GetMapping("/contract/{contractId}")
    public String signatureMem(HttpServletResponse response,@PathVariable Long contractId,Model model,Principal principal){
        String id = "";
        String url="redirect:/";
        if(principal!=null){
            id= principal.getName();
            model.addAttribute("id",principal.getName());
            if(educationService.authCheck(contractId,id,7)){
                service.getContract(response,model,contractId,id);
                url="recruit/contractSign";
            }
        }


        return url;
    }
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_USER')")
    @PostMapping("/saveSignature")
    public ResponseEntity<Map<String,String>> saveSignature(@RequestParam("signature") MultipartFile signature,@RequestParam("contractId") Long contractId,Principal principal) throws IOException{

        //시큐리티 세션으로 처리해야함
        String sid = "";
        if(principal!=null){
            sid= principal.getName();
        }
        Map<String,String> sign = service.saveSignature(signature,contractId,sid);
        return ResponseEntity.ok(sign);
    }
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_USER')")
    @GetMapping("/pdf")
    public @ResponseBody String stest(HttpServletResponse response,Long id){
        service.contractPDF(response, id);
        return"recruit/companySign";
    }

}
