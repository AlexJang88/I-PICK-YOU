package com.project.pickyou.service;

import com.google.gson.JsonObject;
import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.JoinUserDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.repository.CompanyInfoJPARepository;
import com.project.pickyou.repository.MemberInfoJAPRepository;
import com.project.pickyou.repository.MemberJPARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService, UserDetailsService {

    @Value("${lprofile.upload.path}")
    private String profileImgUploadPath;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MemberJPARepository memberJPARepository;
    private final MemberInfoJAPRepository memberInfoJAPRepository;
    private final CompanyInfoJPARepository companyInfoJPARepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override  //유저 회원가입
    public void joinProcess(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO, MultipartFile file) {

        String id = memberDTO.getId();
        Boolean isExist = memberJPARepository.existsById(id);   //중복된 아이디가 있는지 확인하기
        if(isExist){   //중복된값이 있다면 그냥 내보내기
            return;   //로그인화면으로
        }
        //중복된 값이 없다면 회원가입 후 로그인 화면으로
        memberDTO.setAuth("ROLE_USER");
        memberDTO.setPw(bCryptPasswordEncoder.encode(memberDTO.getPw()));

        /*프로필 사진 넣기*/

        if(file.getContentType().startsWith("image")){  //이미지라면
            String originalName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String ext = originalName.substring(originalName.lastIndexOf("."));
            String profileName =uuid+ext;
           // profileImgUploadPath;  //경로 (프로파일까지 만들어져있음/ lprofile.upload.path=C:/Users/upload/profile)
            String saveName = profileImgUploadPath + File.separator + uuid + ext;
            Path savePath = Paths.get(saveName);
            try {
                file.transferTo(savePath); //경로에 저장한다
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberDTO.setProfile(profileName);
        }
        /*프로필 사진 넣기*/


        memberJPARepository.save(memberDTO.toMemberEntity());
        memberInfoJAPRepository.save(memberInfoDTO.toMemberInfoEntity());
    }



    //아이디 중복체크용  ajax, 제이슨   <맞다, 아니다 결과값 반환>
    @Override
    public boolean  checkIfIdExists(String id) {


        Boolean isExist = memberJPARepository.existsById(id);   //중복된 아이디가 있는지 확인하기

        return isExist;
    }




    //사업자 회원가입
    @Override
    public void joinCompanyProcess(MemberDTO memberDTO, CompanyInfoDTO companyInfoDTO, MultipartFile file) {
        String id = memberDTO.getId();

        Boolean isExist = memberJPARepository.existsById(id);   //중복된 아이디가 있는지 확인하기
        if(isExist){   //중복된값이 있다면 그냥 내보내기
            return;   //로그인화면으로
        }
        //중복된 값이 없다면 회원가입 후 로그인 화면으로
        memberDTO.setAuth("ROLE_COMPANY");
        memberDTO.setPw(bCryptPasswordEncoder.encode(memberDTO.getPw()));


        /*프로필 사진 넣기*/

        if(file.getContentType().startsWith("image")){  //이미지라면
            String originalName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String ext = originalName.substring(originalName.lastIndexOf("."));
            String profileName =uuid+ext;
            // profileImgUploadPath;  //경로 (프로파일까지 만들어져있음/ lprofile.upload.path=C:/Users/upload/profile)
            String saveName = profileImgUploadPath + File.separator + uuid + ext;
            Path savePath = Paths.get(saveName);
            try {
                file.transferTo(savePath); //경로에 저장한다
            } catch (IOException e) {
                e.printStackTrace();
            }
            memberDTO.setProfile(profileName);
        }
        /*프로필 사진 넣기*/


        memberJPARepository.save(memberDTO.toMemberEntity());
        companyInfoJPARepository.save(companyInfoDTO.toCompanyInfoEntity());


    }


    //이메일로 아이디 찾기
    @Override
    public String findId(String email) {
        Optional<MemberEntity> memberOptional = memberJPARepository.findByEmail(email);  //이메일로 멤버찾기
        if (memberOptional.isPresent()) {  //멤버가 있다면
            MemberEntity member = memberOptional.get();  //해당하는 맴버의 전체정보를 멤버에 담음
            return member.getId(); // 맴버의 아이디를 넣어라
        } else {
            return "아이디를 찾을 수 없습니다.";
        }

    }


    //이메일과 아이디로 회원 체크
    @Override
    public Boolean checkPW(String email, String id) {

        Optional<MemberEntity> memberOptional = memberJPARepository.findByIdAndEmail(id,email);  //이메일로 멤버찾기
        if (memberOptional.isPresent()) {  //멤버가 있다면
            return true; // 맴버의 아이디를 넣어라
        } else {
            return false;
        }
    }

    //아이디로 확인 후 비밀번호 변경
    @Override
    @Transactional
    public void changePw(MemberDTO memberDTO, String newPw) {
        String userID = memberDTO.getId();
        Optional<MemberEntity> memberOptional = memberJPARepository.findById(userID); // 회원을 찾음

        if (memberOptional.isPresent()) {
            MemberEntity user = memberOptional.get();

            // 새로운 비밀번호를 암호화하여 설정
            String encryptedPassword = passwordEncoder.encode(newPw);
            user.setPw(encryptedPassword);

            memberJPARepository.save(user);
        } else {
            throw new IllegalArgumentException("해당 아이디를 가진 멤버를 찾을 수 없습니다.");
        }
    }














    //회원인지 아닌지 확인해서 권한과 아이디를 넘겨주는것 (JoinUserDTO)에서 확인가능
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Optional<MemberEntity> optionalMember = memberJPARepository.findById(id);
        if(optionalMember.isPresent()) {
            return new JoinUserDTO(optionalMember.get());
        }

        return null;
    }







}
