

package com.project.pickyou.controller.all;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.TrainningDTO;
import com.project.pickyou.entity.TrainningEntity;
import com.project.pickyou.service.TrainningService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/trainning/*")
public class TrainningController {


    private final TrainningService trainningService;

    public TrainningController(TrainningService trainningService){
        this.trainningService =trainningService;
    }

    @GetMapping("/trainningmain")
    public String trainningmain(Model model){  //훈련소 정보 보는곳


        trainningService.trainningCompany(model);

        return "/trainning/trainningmain";
    }


        @GetMapping ("/trainningDetails/{trainnignum}")
        public String trainningDetails(Model model,@PathVariable Long trainnignum){ //훈련소 상세내용

        trainningService.Details(model, trainnignum);//훈련소 내용상세보기

        return  "/trainning/trainningDetails";
        }


        @GetMapping("/trainninginput")
        public String trainninginput(){   //훈련소 추가하기

        return "/trainning/trainninginput";
        }


        @PostMapping("trainninginputPro")
        public String trainninginputPro(TrainningDTO trainningDTO, ImageDTO imageDTO, @RequestParam("files") MultipartFile[] files){

        String companyId = "nine";  //임시값
        trainningDTO.setCompanyId(companyId);

            // 훈련소 정보 저장
            TrainningEntity savedTrainning = trainningService.savetrainning(trainningDTO);

            // 위에서 이미지 파일에 등록된 훈련소 번호의 숫자를 가져옴
            Long trainningId = savedTrainning.getId();

            imageDTO.setBoardNum(trainningId);  // 이미지에 번호 설정하기
            trainningService.saveImage(imageDTO,files);  // 신규 사진값 넣기 실행

        return "redirect:/trainning/trainningmain";
        }


}
