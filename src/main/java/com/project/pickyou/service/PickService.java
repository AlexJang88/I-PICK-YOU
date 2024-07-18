package com.project.pickyou.service;

import org.springframework.ui.Model;

public interface PickService {
    public void myList(Model model,String id,int pageNum);
}
