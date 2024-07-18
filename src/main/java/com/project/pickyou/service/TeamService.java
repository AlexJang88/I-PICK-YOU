package com.project.pickyou.service;

import com.project.pickyou.dto.EducationDTO;
import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.dto.TeamResumeDTO;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamService {
    public void AllPosts(Model model, int pageNum);
    public void post(Model model,Long boardNum,String sid,int boardType);
    public void writePost(MultipartFile profile,List<MultipartFile> files, TeamResumeDTO dto,int boardType);
    public void update(MultipartFile profile,List<MultipartFile> files, TeamResumeDTO dto,int boardType);
    public int favoriteCheck(PickDTO dto);
    public void exposure(TeamResumeDTO dto);
    public void MyPosts(Model model ,String id,int boardType);
    public boolean checkMyPost(String id);
}
