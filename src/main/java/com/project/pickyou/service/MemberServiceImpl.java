package com.project.pickyou.service;

import com.project.pickyou.dto.CompanyInfoDTO;
import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.dto.MemberInfoDTO;
import com.project.pickyou.entity.CompanyInfoEntity;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.entity.MemberInfoEntity;
import com.project.pickyou.repository.CompanyInfoJPARepository;
import com.project.pickyou.repository.ImageJPARepository;
import com.project.pickyou.repository.MemberInfoJAPRepository;
import com.project.pickyou.repository.MemberJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

        @Value("${lprofile.upload.path}")
        private String profileImgUploadPath;

        private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        private final MemberJPARepository memberJPARepository;
        private final MemberInfoJAPRepository memberInfoJAPRepository;
        private final CompanyInfoJPARepository companyInfoJPARepository;


        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;


        //마이페이지에 회원정보가져오기;
        @Override
        public void findUserInfo(String id, Model model) {
            Optional<MemberEntity> user = memberJPARepository.findById(id);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
            } else {
            }
        }


        //회원삭제
        @Override
        public void deleteUser(MemberDTO memberDTO) {

            memberJPARepository.deleteById(memberDTO.getId());

        }


        //일반 유저 수정
        @Override
        public void updateUser(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO, MultipartFile file) {

            String id = memberDTO.getId(); //아이디값 찾음
            Optional<MemberEntity> userget = memberJPARepository.findById(id);  //우선 회원정보를 찾아옴
            Optional<MemberInfoEntity> userInfoget = memberInfoJAPRepository.findById(id);  //우선 회원정보를 찾아옴


            if (userget.isPresent() && userInfoget.isPresent()) {  //두 값이 실제로 디비에 존재할때
                MemberInfoEntity userinfo = userInfoget.get();  //멤버 인포 담기
                MemberEntity user = userget.get();   //멤버 담기

                userinfo.setName(memberInfoDTO.getName());
                userinfo.setBirth(memberInfoDTO.getBirth());
                userinfo.setHeight(memberInfoDTO.getHeight());
                userinfo.setWeight(memberInfoDTO.getWeight());
                userinfo.setHealth(memberInfoDTO.getHealth());
                userinfo.setGender(memberInfoDTO.getGender());

                memberInfoJAPRepository.save(userinfo);  //우선 맴버 인포부터저장

                //아래는 멤버 담기
                //우선 사진부터

                if (!file.isEmpty()) { //수정 파일에 사진이 들어있다면 폴더속 사진삭제하는 작업
                    File profile = new File(profileImgUploadPath + File.separator + user.getProfile());  //유저 프로필의 경로
                    if (!user.getProfile().equals("default.png")) {  //사진이름이 디폴트가 아니라면 , 디폴트라면 놔둠
                        try {
                            if (profile.exists()) {
                                profile.delete();   //폴더속 프로필 삭제
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                if (!file.isEmpty()) { //수정 파일에 사진이 있다면 폴더에 넣는 작업
                    String originalName = file.getOriginalFilename();
                    String uuid = UUID.randomUUID().toString();
                    String ext = originalName.substring(originalName.lastIndexOf("."));
                    String profileName = uuid + ext;

                    memberDTO.setProfile(profileName);
                    user.setProfile(profileName);//디비 이름 설정하기
                    Path savePath = Paths.get(profileImgUploadPath, profileName);

                    try {
                        file.transferTo(savePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    user.setPw(bCryptPasswordEncoder.encode(memberDTO.getPw()));
                    user.setAddress(memberDTO.getAddress());
                    user.setPhone(memberDTO.getPhone());
                    user.setEmail(memberDTO.getEmail());

                    //해당 자료들은 변경된 내용을 넣는다
                    memberJPARepository.save(user);

                }else {
                    //오류없음
                }
            }
        }




    //사업자 정보수정
    @Override
    public void updateCompany(MemberDTO memberDTO, CompanyInfoDTO companyInfoDTO, MultipartFile file) {
        String id = memberDTO.getId(); //아이디값 찾음
        Optional<MemberEntity> userget = memberJPARepository.findById(id);  //우선 회원정보를 찾아옴
        Optional<CompanyInfoEntity> companyInfoget = companyInfoJPARepository.findById(id);  //우선 회원정보를 찾아옴

        if (userget.isPresent() && companyInfoget.isPresent()) {
            CompanyInfoEntity companyInfo = companyInfoget.get();  //회사 상세정보 담기
            MemberEntity user = userget.get();   //멤버 정보 담기

            companyInfo.setName(companyInfoDTO.getName());
            companyInfo.setCorpno(companyInfoDTO.getCorpno());
            companyInfo.setJob(companyInfoDTO.getJob());
            companyInfo.setCompanyName(companyInfoDTO.getCompanyName());

            companyInfoJPARepository.save(companyInfo); //사업자 상세 저장

            //아래는 멤버 담기
            //우선 사진부터

            if (!file.isEmpty()) { //수정 파일에 사진이 들어있다면 폴더속 사진삭제하는 작업
                File profile = new File(profileImgUploadPath + File.separator + user.getProfile());  //유저 프로필의 경로
                if (!user.getProfile().equals("default.png")) {  //사진이름이 디폴트가 아니라면 , 디폴트라면 놔둠
                    try {
                        if (profile.exists()) {
                            profile.delete();   //폴더속 프로필 삭제
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            if (!file.isEmpty()) { //수정 파일에 사진이 있다면 폴더에 넣는 작업
                String originalName = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String ext = originalName.substring(originalName.lastIndexOf("."));
                String profileName = uuid + ext;

                memberDTO.setProfile(profileName);
                user.setProfile(profileName);//디비 이름 설정하기
                Path savePath = Paths.get(profileImgUploadPath, profileName);

                try {
                    file.transferTo(savePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                user.setPw(bCryptPasswordEncoder.encode(memberDTO.getPw()));
                user.setAddress(memberDTO.getAddress());
                user.setPhone(memberDTO.getPhone());
                user.setEmail(memberDTO.getEmail());

                //해당 자료들은 변경된 내용을 넣는다
                memberJPARepository.save(user);

            } else {
                //오류없음
            }
        }
    }






}


