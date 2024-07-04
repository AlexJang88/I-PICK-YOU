package com.project.pickyou.service;

import com.project.pickyou.dto.JoinUserDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.repository.MemberInfoJAPRepository;
import com.project.pickyou.repository.MemberJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService, UserDetailsService {

    private final MemberJPARepository memberJPARepository;
    private final MemberInfoJAPRepository memberInfoJAPRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override  //유저 회원가입
    public void joinProcess(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO) {

        String id = memberDTO.getId();

        Boolean isExist = memberJPARepository.existsById(id);   //중복된 아이디가 있는지 확인하기

        if(isExist){   //중복된값이 있다면 그냥 내보내기
            return;   //로그인화면으로
        }

        //중복된 값이 없다면 회원가입 후 로그인 화면으로
        memberDTO.setAuth("ROLE_ADMIN");
        memberDTO.setPw(bCryptPasswordEncoder.encode(memberDTO.getPw()));

        memberJPARepository.save(memberDTO.toMemberEntity());
        memberInfoJAPRepository.save(memberInfoDTO.toMemberInfoEntity());
    }


    //회원인지 아닌지 확인해서 권한과 아이디를 넘겨주는것 (JoinUserDTO)에서 확인가능
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        MemberEntity userdata = memberJPARepository.findByid(id);
        if(userdata != null){

            return new JoinUserDTO(userdata);
        }

        return null;
    }
















    /*@Override
    public boolean searchUser(String uid, String upw) {  //로그인 성공여부
         String id = uid;
         String pw = upw;

        Optional<MemberEntity> realUser = memberJPARepository.findByIdAndPw(id, pw);

        // optionalUser가 값이 존재하면 true, 아니면 false를 반환
        return realUser.isPresent();
    }
*/






}
