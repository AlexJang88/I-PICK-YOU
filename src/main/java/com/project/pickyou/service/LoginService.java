package com.project.pickyou.service;

import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import org.springframework.web.multipart.MultipartFile;

public interface LoginService {

    public void joinProcess(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO, MultipartFile file);  //유저회원가입

    boolean checkIfIdExists(String id);  //아이디찾기

    public void joinCompanyProcess(MemberDTO memberDTO, CompanyInfoDTO companyInfoDTO, MultipartFile file); //사업자회원가입

    public String findId(String email);

    public Boolean checkPW(String email, String id);

   public void changePw(MemberDTO memberDTO, String newPw);

    boolean checkIfEmailExists(String email);  //이메일 중복체크

    boolean checkIfcorpnocheck(String corpno);  //사업자 중복체크
}
