package com.project.pickyou.service;

import org.springframework.ui.Model;

public interface SatisfactionService {
    public int existCheck(String writer, String target, Model model);
    public void myScore(Model model,String id);
}
