package com.project.pickyou.service;


import com.project.pickyou.dto.FoodMapDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.entity.FoodMapEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodMapService {

    // 푸드맵 리스트 가져오기, 페이징 처리
    public void AllPosts(Model model, int pageNum);

    // 푸드맵 인서트
    public FoodMapEntity foodMapInsert(FoodMapDTO dto);

    // 푸드맵 이미지 인서트
    public void saveImage(ImageDTO imageDTO, MultipartFile[] files);

    // 푸드맵 글번호의 내용 가져오기
    public void foodMapInfo(int ref, Model model);

    // 푸드맵 이미지 가져오기
    public void foodMapImage(Long boardNum, int boardType, Model model);

    // 푸드맵 글 삭제
    public void foodMapDelete(Long boardNum);

    // 푸드맵 글 수정
    public void foodMapUpdate(Model model, Long num);

    // 푸드맵 글 수정
    public void update(List<MultipartFile> files, FoodMapDTO foodMapDTO);

    // 댓글작성하기
    public void ref(Model model, FoodMapDTO dto);
}
