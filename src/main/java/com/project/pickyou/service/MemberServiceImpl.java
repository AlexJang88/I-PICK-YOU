package com.project.pickyou.service;

import com.project.pickyou.dto.*;
import com.project.pickyou.entity.*;
import com.project.pickyou.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MemberJPARepository memberJPARepository;
    private final MemberInfoJAPRepository memberInfoJAPRepository;
    private final CompanyInfoJPARepository companyInfoJPARepository;
    private final PaymentJPARepository paymentJPA;
    private final RecruitStateJPARepository recruitStateJPA;
    private final ConfirmJPARepository confirmJPA;
    private final S3Service s3Service;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //마이페이지에 회원정보가져오기;
    @Override
    public void findUserInfo(String id, Model model) {
        Optional<MemberEntity> user = memberJPARepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("bucketName", bucket);  //아마존 경로
            model.addAttribute("regionName", region); //아마존 경로
        } else {
        }
    }


    //회원삭제
    @Override
    public void deleteUser(MemberDTO memberDTO) {
        String id = memberDTO.getId();

        Optional<MemberEntity> memberEntity = memberJPARepository.findById(id);
        MemberEntity user = memberEntity.get();   //멤버 담기

        if (memberEntity.isPresent()) {
            s3Service.deleteFile("profile/"+user.getProfile());
        }
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

            if (userget.isPresent()) {
                if (!file.isEmpty()) {
                    if (file.getContentType().startsWith("image")) { //아마존 서버에서 삭제

                        s3Service.deleteFile("profile/" +  user.getProfile());
                        try {
                            String filePath = s3Service.uploadFile(file, "profile");
                            user.setProfile(filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            user.setPw(bCryptPasswordEncoder.encode(memberDTO.getPw()));
            user.setAddress(memberDTO.getAddress());
            user.setPhone(memberDTO.getPhone());
            user.setEmail(memberDTO.getEmail());

            //해당 자료들은 변경된 내용을 넣는다
            memberJPARepository.save(user);
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

            if (userget.isPresent()) {
                if (!file.isEmpty()) {
                    if (file.getContentType().startsWith("image")) { //아마존 서버에서 삭제

                        s3Service.deleteFile("profile/" +  user.getProfile());
                        try {
                            String filePath = s3Service.uploadFile(file, "profile");
                            user.setProfile(filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            user.setPw(bCryptPasswordEncoder.encode(memberDTO.getPw()));
            user.setAddress(memberDTO.getAddress());
            user.setPhone(memberDTO.getPhone());
            user.setEmail(memberDTO.getEmail());

            //해당 자료들은 변경된 내용을 넣는다
            memberJPARepository.save(user);
        }
    }

    // 사업자 결제 내역
    @Override
    public void paymentList(Model model, int pageNum, String memberId, int pointHistory) {
        int pageSize = 10;
        int count = paymentJPA.countByMemberIdAndPointHistory(memberId, pointHistory);

        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<PaymentEntity> page = paymentJPA.findByMemberIdAndPointHistory(memberId, pointHistory, (Pageable) PageRequest.of(pageNum - 1, pageSize, sort));

        List<PaymentEntity> posts = page.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;   //페이징(이전/다음)을 몇개단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
    }



    // 회원의 지원현황 가져오기
    @Override
    public void userRecruitList(String memberId, int pageNum, Model model) {
        int pageSize = 10;
        Long longcount = recruitStateJPA.countByMemberId(memberId);
        int count = longcount.intValue();
        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<RecruitStateEntity> page = recruitStateJPA.findByMemberId(memberId, (Pageable) PageRequest.of(pageNum - 1, pageSize, sort));
        List<RecruitStateEntity> posts = page.getContent();
        model.addAttribute("posts", posts);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;   //페이징(이전/다음)을 몇개단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
    }

    // 유저 입장에서 채용현황 가져오기
    @Override
    public void confirmList(String memberId, int pageNum, Model model) {
        int pageSize = 10;
        int count = confirmJPA.countByMemberId(memberId);


        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<ConfirmEntity> page = confirmJPA.findByMemberId(memberId,PageRequest.of(pageNum - 1, pageSize, sort));

        List<ConfirmEntity> posts = page.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;   //페이징(이전/다음)을 몇개단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
    }

    // 고용 요청 내역(사업자가 요청한것)
    @Override
    public void findByCompanyIdAndApply(String companyId, int apply, int pageNum, Model model) {
        int pageSize = 10;
        int count = confirmJPA.countByCompanyIdAndApply(companyId, apply);

        // Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<ConfirmEntity> page = confirmJPA.findByCompanyIdAndApply(companyId, apply, (Pageable) PageRequest.of(pageNum - 1, pageSize));
        List<ConfirmEntity> posts = page.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;   //페이징(이전/다음)을 몇개단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }

        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
    }

    // 채용 확정
    @Override
    public void findByCompanyIdAndApplyIn(String companyId, int pageNum, Model model) {
        List<Integer> applies = Arrays.asList(1, 2);
        int pageSize = 10;
        int count = confirmJPA.countByCompanyIdAndApplyIn(companyId, applies);

        // Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<ConfirmEntity> page = confirmJPA.findByCompanyIdAndApplyIn(companyId, applies, (Pageable) PageRequest.of(pageNum - 1, pageSize));
        List<ConfirmEntity> posts = page.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;   //페이징(이전/다음)을 몇개단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }

        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
    }

    // 유저 입장에서 출근 확정 업데이트
    @Override
    public void updateApply(ConfirmDTO dto) {

        confirmJPA.save(dto.toConfirmEntity());
    }


}


