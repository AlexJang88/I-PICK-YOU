package com.project.pickyou.service;

import org.springframework.ui.Model;

public interface PointService {

    // 포인트 내역
    public void AllPosts(Model model, int pageNum, String memberId);
}
