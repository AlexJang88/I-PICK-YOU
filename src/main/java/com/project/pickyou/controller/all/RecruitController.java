package com.project.pickyou.controller.all;

import com.project.pickyou.dto.ContractDTO;
import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.dto.RecruitDTO;
import com.project.pickyou.dto.RecruitDetailDTO;
import com.project.pickyou.service.RecruitService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    @Value("${contract.upload.path}")
    private String contactUploadPath;

    private final RecruitService service;

    @GetMapping("/posts")
    public String list(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, Principal principal){
        if(principal!=null) {
            model.addAttribute("memberId", principal.getName());
        }
        service.AllPosts(model,pageNum);
        return "recruit/list";
    }
    @GetMapping("/myPosts")
    public String mylist(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, Principal principal){
        if(principal!=null) {
            model.addAttribute("memberId", principal.getName());
        }
        service.AllPosts(model,pageNum);
        return "recruit/myList";
    }
    @GetMapping("/posts/{boardNum}")
    public String educationsContent(Model model,@PathVariable Long boardNum,Principal principal){
        String sid ="";
        if(principal!=null){
                principal.getName();}
        //principal.getName();
        service.post(model,boardNum,sid);
        return "recruit/content";
    }
    //수정페이지 이동
    @GetMapping("posts/edit/{boardNum}")
    public String edit(Model model,@PathVariable Long boardNum,Principal principal){
        String sid = "";
        if(principal!=null) {
            principal.getName();
        }
        //principal.getName();
        service.post(model,boardNum,sid);
        return "recruit/update";
    }
    //수정
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
    @DeleteMapping("/posts")
    public String delete(@RequestParam(name = "id")Long id){
        service.deletePost(id,6);
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
                             @RequestParam("endDateParam") @DateTimeFormat(pattern = "yyyy-MM-dd")Date endDateParam,Principal principal
    ){
        int check=0;
        check = rdto.getStatus();
        String url="redirect:/recruit/posts";
        if(check==2){
            //결제 페이지로 이동
            url="redirect:/recruit/posts";
        }
        //사업자 아이디 = 로그인 구현후 session 받아와서 다시 처리
        if(principal!=null) {
            rdto.setMemberId(principal.getName());
        }
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
    public String checkFavoritecheck(@PathVariable Long boardNum,@PathVariable String target,Principal principal){
        String sid = "";
        if(principal!=null) {
            principal.getName();
        }
        //principal.getName(); 로그인 적용후 번경
        PickDTO dto = new PickDTO();
        dto.setPicker(sid);
        dto.setTarget(target);
        String url = "redirect:/recruit/posts/"+boardNum;
        service.favoriteCheck(dto);
        return url;
    }
    @PostMapping("/apply/{boardNum}")
    public String apply(@PathVariable Long boardNum,Principal principal){
        //로그인 처리후 세션으로 변경
        String sid = "";
        if(principal!=null) {
            principal.getName();
        }
        String url="redirect:/recruit/posts/"+boardNum;
        if(service.recruit(boardNum,sid)==0){
            //추후에 이력서 등록페이지 만들어지면 url 바꿔야함
            url="redirect:/recruit/posts";
        }
        return url;
    }
    @GetMapping("/contract/{memberId}/{stateId}")
    public String contract(Model model,Principal principal,@PathVariable String memberId,@RequestParam int type,@PathVariable Long stateId){
        String url="";
        String name ="";
        if(principal!=null) {
             name=principal.getName();
        }
        if(type==1){
                url="recruit/contractForm";

            service.userInfo(model,memberId,

                    name,stateId);
            }
        else if(type==2){
            service.basicContract(memberId,name,1,stateId);
            url="redirect:/recruitState/posts";
        }else if(type==3){

        }


        return url;
    }
    @PostMapping("/contract/company/{stateId}")
    public String signature(@ModelAttribute ContractDTO dto,@PathVariable Long stateId){
        Long id=service.contract(dto,stateId);
        String url = "redirect:/contract/sign/"+id;
        return url;
    }
    @GetMapping("/contract/{contractId}")
    public String signatureMem(HttpServletResponse response,@PathVariable Long contractId,Model model,Principal principal){
        //String id = principal.getName();
        String id = "two";
        service.getContract(response,model,contractId,id);

        return"recruit/contractSign";
    }

    @PostMapping("/saveSignature")
    public ResponseEntity<Map<String,String>> saveSignature(@RequestParam("signature") MultipartFile signature,@RequestParam("contractId") Long contractId) throws IOException{

        //시큐리티 세션으로 처리해야함
        String sid = "two";
        Map<String,String> sign = service.saveSignature(signature,contractId,sid);
        return ResponseEntity.ok(sign);
    }
    @GetMapping("/pdf")
    public @ResponseBody String stest(HttpServletResponse response,Long id){


        service.contractPDF(response, id);
        return"recruit/companySign";
    }
    @GetMapping("/stest")
    public String cset(){
        return "recruit/sign";
    }

}
