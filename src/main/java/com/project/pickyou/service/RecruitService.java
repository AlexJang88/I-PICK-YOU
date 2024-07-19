package com.project.pickyou.service;

import com.project.pickyou.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RecruitService {
    public void AllPosts(Model model, int pageNum,int checkType);
    public void myPosts(Model model,int pageNum,String id);
    public void post(Model model,Long num,String sid);
    public void writePost(List<MultipartFile> files, RecruitDTO rdto, RecruitDetailDTO rddto, int boardType);
    public void deletePost(Long boardNum,int boardType);
    public void update(List<MultipartFile> files,RecruitDTO dto,RecruitDetailDTO rddto,int boardType);
    public int favoriteCheck(PickDTO dto);
    public int recruit(Long boardNum,String id);
    public void contractPDF(HttpServletResponse response,Long id);
    public void userInfo(Model model,String memberId,String companyId,Long stateId,int type);
    public Long contract(ContractDTO dto ,Long stateId,int applyType);
    public void getContract(HttpServletResponse response,Model model,Long id,String userId);
    public Map<String,String> saveSignature(MultipartFile multipartFile,Long contractId,String userId);
    public void basicContract(String memberId,String companyId,int type,Long stateId);
    public void updateReadCount(Long boardNum);
}
