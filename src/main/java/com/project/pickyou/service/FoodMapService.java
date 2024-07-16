package com.project.pickyou.service;


import com.project.pickyou.dto.FoodMapDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.PointDTO;
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

    // 푸드맵 조회수 증가
    public void foodMapCnt(Long id, int ref, Model model);

    // 푸드맵 댓글 인서트
    public FoodMapEntity refInsert(FoodMapDTO dto, int ref);

    // 푸드맵 대댓글 인서트
    public FoodMapEntity replyInsert(FoodMapDTO dto);

    // 푸드맵 인증글 포인트 인서트
    public void foodMapPointInsert(PointDTO dto, FoodMapDTO FMdto);
}
