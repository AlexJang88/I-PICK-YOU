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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        memberDTO.setAuth("ROLE_ADMIN");
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
