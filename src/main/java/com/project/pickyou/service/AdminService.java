package com.project.pickyou.service;

import org.springframework.ui.Model;

public interface AdminService {
   /* // 포인트 지급 내역
    public void AllPosts(Model model, int status, int pageNum);

    // 포인트 차감 내역
    public void AllPost(Model model, int status, int pageNum);*/

    public void getUsre(Model model, int pageNum); //일반회원정보 가져오기

    public void userDelete(String id); //일반유저 삭제

    public void getCompany(Model model, int pageNum); //사업자 유저 정보 가져오기

    // 포인트 지급, 사용 내역
    public void AllPostsGIVE(Model model, int status, int pageNum, int month);

    // 결제, 포인트 사용내역 가져오기
    public void AllpaymentANDpoint(Model model, int pointHistory, int pageNum, int month);

    // TEST ////////////////////////
    public void TEST(Model model, int pointHistory, int month, String chartType);
}
