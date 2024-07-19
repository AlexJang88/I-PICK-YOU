package com.project.pickyou.service;

import org.springframework.ui.Model;

public interface AdminService {
    // 포인트 지급 내역
    public void AllPosts(Model model, int status, int pageNum);

    // 포인트 차감 내역
    public void AllPost(Model model, int status, int pageNum);

}
