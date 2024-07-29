package com.project.pickyou.service;

import com.project.pickyou.dto.PointDTO;
import org.springframework.ui.Model;

public interface PointService {

    // 포인트 변환 인서트
    public void pointInsert(PointDTO dto);

    // 포인트 내역
    public void AllPosts(Model model, int pageNum, int status, String memberId);
}
