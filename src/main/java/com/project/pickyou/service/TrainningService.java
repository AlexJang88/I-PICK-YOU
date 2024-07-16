package com.project.pickyou.service;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.TrainningDTO;
import com.project.pickyou.entity.TrainningEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface TrainningService {


    public void trainningCompany(Model model, int pageNum);  //훈련소 리스트 가져오기

    public void Details(Model model, Long trainnignum); //훈련소 내용 상세보기

    public TrainningEntity savetrainning(TrainningDTO trainningDTO);  //훈련소 추가하기

    public void saveImage(ImageDTO imageDTO, MultipartFile[] files); //이미지 넣기 번호랑 사진 가져가기

    public void deleteDetails(Long trainnignum);  //훈련소 내용 지우기

    public void deleteDetailsImg(Long trainnignum);  //이미지 지우기

    public void trainningUpdate(Long trainnignum, TrainningDTO trainningDTO, MultipartFile[] files); //사진, 내용 업데이트


    public void trainngCount(Long trainnignum); //훈련소 조회수
}
