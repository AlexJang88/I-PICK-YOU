package com.project.pickyou.service;

import com.project.pickyou.dto.MemberDTO;
import org.springframework.ui.Model;

public interface MemberService {


    public void findUserInfo(String id, Model model); //회원정보 불러오기

    public void deleteUser(MemberDTO memberDTO);//회원삭제
}
