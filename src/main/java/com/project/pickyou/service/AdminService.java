package com.project.pickyou.service;

import com.project.pickyou.dto.PointDTO;
import org.springframework.ui.Model;

public interface AdminService {
    // 포인트 지급 내역
    public void AllPosts(Model model, int status, int pageNum);

    // 포인트 차감 내역
    public void AllPost(Model model, int status, int pageNum);


    public void getUsre(Model model, int pageNum); //일반회원정보 가져오기

    public void userDelete(String id); //일반유저 삭제

    public void getCompany(Model model, int pageNum); //사업자 유저 정보 가져오기

    public void getnPoitApproval(Model model, int pageNum); //포인트 승인하는곳

    public void patchPoint(PointDTO pointDTO); //포인트 변환
}
