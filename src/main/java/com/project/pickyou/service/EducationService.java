package com.project.pickyou.service;

import com.project.pickyou.dto.EducationDTO;
import com.project.pickyou.dto.PickDTO;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EducationService {
    public void AllPosts(Model model,int pageNum);
    public void post(Model model,Long num,String sid,int boardType);
    public void writePost(List<MultipartFile> files, EducationDTO dto,int boardType);
    public void deletePost(Long boardNum,int boardType);
    public void update(List<MultipartFile> files,EducationDTO dto,int boardType);
    public int favoriteCheck(PickDTO dto);
}
