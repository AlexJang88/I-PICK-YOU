package com.project.pickyou.service;

import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {


    public void findUserInfo(String id, Model model); //회원정보 불러오기

    public void deleteUser(MemberDTO memberDTO);//회원삭제

    public void updateUser(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO, MultipartFile file); //유저정보수정

    public void updateCompany(MemberDTO memberDTO, CompanyInfoDTO companyInfoDTO, MultipartFile file);  //사업자정보수정

    // 사업자 결제내역
    public void paymentList(Model model, int pageNum, String memberId);

    // 회원의 지원현황 가져오기
    public void userRecruitList(String memberId, int pageNum, Model model);

    // 유저 입장에서 채용현황 가져오기
    public void confirmList(String memberId, int pageNum, Model model);
}
