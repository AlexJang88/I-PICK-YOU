package com.project.pickyou.service;

import com.project.pickyou.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecruitService {
    public void AllPosts(Model model, int pageNum);
    public void post(Model model,Long num,String sid);
    public void writePost(List<MultipartFile> files, RecruitDTO rdto, RecruitDetailDTO rddto, int boardType);
    public void deletePost(Long boardNum,int boardType);
    public void update(List<MultipartFile> files,RecruitDTO dto,RecruitDetailDTO rddto,int boardType);
    public int favoriteCheck(PickDTO dto);
    public int recruit(Long boardNum,String id);
    public void contract(HttpServletResponse response);
    public void userInfo(Model model,String memberId,String companyId);
    public Long contract(ContractDTO dto );
    public void getContract(Model model,Long id);
}
