package com.project.pickyou.service;

import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.repository.ImageJPARepository;
import com.project.pickyou.repository.MemberJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberJPARepository memberJPARepository;
    private final ImageJPARepository imageJPARepository;

    //마이페이지에 회원정보가져오기;
    @Override
    public void findUserInfo(String id, Model model) {
        Optional<MemberEntity> user = memberJPARepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        } else { }
    }


    //회원삭제
    @Override
    public void deleteUser(MemberDTO memberDTO) {

        memberJPARepository.deleteById(memberDTO.getId());

    }


}
