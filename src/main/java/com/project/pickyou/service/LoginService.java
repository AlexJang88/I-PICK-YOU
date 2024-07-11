package com.project.pickyou.service;

import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import org.springframework.web.multipart.MultipartFile;

public interface LoginService {

    public void joinProcess(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO, MultipartFile file);  //유저회원가입

    boolean checkIfIdExists(String id);  //아이디찾기

    public void joinCompanyProcess(MemberDTO memberDTO, CompanyInfoDTO companyInfoDTO, MultipartFile file); //사업자회원가입
}
