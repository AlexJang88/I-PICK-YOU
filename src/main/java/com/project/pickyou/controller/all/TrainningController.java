

package com.project.pickyou.controller.all;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.TrainningDTO;
import com.project.pickyou.entity.TrainningEntity;
import com.project.pickyou.service.TrainningService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/trainning/*")
public class TrainningController {


    private final TrainningService trainningService;

    private int type=3;

    public TrainningController(TrainningService trainningService){
        this.trainningService =trainningService;
    }

    @GetMapping("/posts")
    public String trainningmain(Model model, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, Principal pc){  //훈련소 정보 보는곳
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }
        trainningService.trainningCompany(model, pageNum);

        return "trainning/trainningmain";
    }

    //훈련소 내용상세보기
        @GetMapping ("/posts/{trainnignum}")
        public String trainningDetails(Model model, @PathVariable Long trainnignum, Principal principal,
                                       HttpSession session, HttpServletRequest request
                                       ){ //훈련소 상세내용

            if(principal != null){
                model.addAttribute("id", principal.getName()) ;
            }

            String sid="";
            String ip=request.getHeader("X-FORWARDED-FOR");
            if(ip==null){
                ip=request.getRemoteAddr();
            }

            if(principal != null){

                sid = principal.getName();
                model.addAttribute("pid", sid);
                if(session.getAttribute(sid+"_"+type+"_"+trainnignum)==null){
                    trainningService.trainngCount(trainnignum);
                    session.setAttribute(sid+"_"+type+"_"+trainnignum,"true");
                }
            }else {   //로그인안되어있을때 조회수 올리기
                sid = ip;
                if (session.getAttribute(sid + "_" + type + "_" + trainnignum) == null) {
                    trainningService.trainngCount(trainnignum);
                    session.setAttribute(sid + "_" + type + "_" + trainnignum, "true");
                }

            }


        trainningService.Details(model, trainnignum);//훈련소 내용상세보기

        return  "trainning/trainningDetails";
        }




     /* 아래는 훈련소 내용 삭제 및 수정 */

    //훈련소 내용 삭제하기
    @DeleteMapping("/posts/{trainnignum}")
    public String trainningdelete(@PathVariable Long trainnignum) {

        trainningService.deleteDetailsImg(trainnignum); //이미지 지우기

        // 삭제 후에는 다시 상세 페이지로 리다이렉트
        return "redirect:/trainning/posts";
    }

    //훈련소 내용 수정하기

    @GetMapping("/posts/{trainnignum}/edit")
    public String trainninginUpdate(Model model,@PathVariable Long trainnignum, Principal pc){
            //작성된 기록 가져오기
        if(pc != null){
            model.addAttribute("id",pc.getName()) ;
        }

        trainningService.Details(model, trainnignum);

        return "trainning/trainningUpdate";
    }

    //업데이트 프로
    @PatchMapping("/posts/{trainnignum}")
    public String trainningUpdatePro(@PathVariable Long trainnignum,TrainningDTO trainningDTO, @RequestParam("files") MultipartFile[] files){ //사진, dto, 글번호 가져오기

        trainningService.trainningUpdate(trainnignum, trainningDTO, files);
        return "redirect:/trainning/posts/"+trainnignum;
    }



        /*아래는 훈련소 신규 내용 추가하기 페이지  / 사진넣기페이지*/
        @GetMapping("/posts/new")
        public String trainninginput(Principal pc, Model model){

            if(pc != null){
                model.addAttribute("id",pc.getName()) ;
            }

        return "trainning/trainninginput";
        }

        @PostMapping("/posts")
        public String trainninginputPro(TrainningDTO trainningDTO, @RequestParam("files") MultipartFile[] files, Principal principal){

            String companyId = principal.getName();
            trainningDTO.setCompanyId(companyId);

            trainningService.savetrainning(trainningDTO, files);

        return "redirect:/trainning/posts";
        }



}
