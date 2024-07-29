package com.project.pickyou.service;

import com.project.pickyou.dto.EducationDTO;
import com.project.pickyou.dto.PickDTO;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface EducationService {
    public void AllPosts(Model model,int pageNum);
    public void post(Model model,Long num,String sid,int boardType);
    public void writePost(List<MultipartFile> files, EducationDTO dto,int boardType);
    public void deletePost(Long boardNum,int boardType);
    public int authCheck(Principal principal);
    public void update(List<MultipartFile> files,EducationDTO dto,int boardType);
    public int favoriteCheck(PickDTO dto);
    public void updateReadCount(Long boardNum);
    public boolean authCheck(Long boardNum,String id,int type);
}
