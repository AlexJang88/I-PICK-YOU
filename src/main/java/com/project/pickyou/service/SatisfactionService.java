package com.project.pickyou.service;

import com.project.pickyou.dto.SatisfactionDTO;
import org.springframework.ui.Model;

public interface SatisfactionService {
    public int existCheck(String writer, String target, Model model);
    public void myScore(Model model,String id);
    public void scoreSet(SatisfactionDTO dto);
    public void scoreEdit(SatisfactionDTO dto);
    public void scoreList(Model model,String sid,int pageNum);
}
