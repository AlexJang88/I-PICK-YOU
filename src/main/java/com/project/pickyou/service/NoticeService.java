package com.project.pickyou.service;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.NoticeDTO;
import com.project.pickyou.entity.NoticeEntity;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    // 공지사항 리스트 가져오기, 페이징 처리
    public void AllPosts(Model model, int pageNum);
/*

    // 공지사항 인서트
    public NoticeEntity noticeInsert(NoticeDTO dto);
*/

    // 공지사항 이미지 인서트
    public void saveImage(NoticeDTO dto, List<MultipartFile> files);

    // 공지사항 이미지 가져오기
    public void noticeImage(Long boardNum, int boardType, Model model);

    // 공지사항 글번호의 내용 가져오기
    public void noticeInfo(Long id, Model model);

    // 공지사항 글 삭제
    public void noticeDelete(Long boardNum);

    // 공지사항 글 수정
    public void noticeUpdate(Model model, Long num);

    // 공지사항 글 수정
    public void update(List<MultipartFile> files, NoticeDTO dto);

    // 공지사항 조회수 증가
    public void noticeCnt(Long id, Model model);

    // 메인에 공지사항 최신글 1개
    public void mainNotice(Model model);
}
