package com.project.pickyou.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.pickyou.dto.*;
import com.project.pickyou.entity.*;
import com.project.pickyou.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;



    private final S3Service s3Service;

    private final EducationJPARepository educationJPA;
    private final ImageJPARepository imageJPA;
    private final PickJPARepository pickJPA;
    private final MemberJPARepository memberJPA;
    private final TeamResumeJPARepository teamResumeJPA;
    private final RecruitJPARepository recruitJPA;
    private final ContractJPARepository contractJPA;


    @Override
    public void AllPosts(Model model, int pageNum) {
        int type=0;
        int pageSize = 9;
        Long longCount = educationJPA.count();
        int count = longCount.intValue();
        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<EducationEntity> page = educationJPA.findAll(PageRequest.of(pageNum - 1, pageSize, sort));

        List<EducationEntity> posts = page.getContent();

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
    public int getfavoritStatus(String picker,String target){
        int result=0;
        PickID key = new PickID(picker, target);
        key.setPicker(picker);
        key.setTarget(target);
        Optional<PickEntity> pickcheck = pickJPA.findById(key);
        if (pickcheck.isPresent()) {
            result = 1;
        }
        return result;
    }
    @Override
    public void post(Model model, Long num, String sid,int boardType) {
        int type=0;
        int auth=0;
        Optional<EducationEntity> post = educationJPA.findById(num);
        Optional<MemberEntity> member =memberJPA.findById(sid);
        EducationDTO edto = new EducationDTO();
        CompanyInfoDTO cidto = new CompanyInfoDTO();
        MemberDTO mdto = new MemberDTO();
        List<ImageEntity> imageList = Collections.emptyList();
        int favoritecheck = 0;
        if(member.isPresent()) {
            if (member.get().getAuth().contains("USER")) {
                auth = 1;
            } else if (member.get().getAuth().contains("COMPANY")) {
                auth = 2;
            } else if (member.get().getAuth().contains("ADMIN")) {
                auth = 99;
            }

        }


        if (post.isPresent()) {
            if(post.get().getCompanyId().equals(sid)){
                type=1;
            }
            imageList = imageJPA.findByBoardTypeAndBoardNum(boardType, num);
            edto = post.get().toEducationDTO();
            cidto = post.get().getMember().getCompanyInfo().toCompanyInfoDTO();
            mdto = post.get().getMember().toMemberDTO();
            favoritecheck=getfavoritStatus(sid,post.get().getCompanyId());
            edto.setContent(edto.getContent().replace("<br>", "\r\n"));
            model.addAttribute("auth",auth);
            model.addAttribute("favoriteCheck", favoritecheck);
            model.addAttribute("member", mdto);
            model.addAttribute("company", cidto);
            model.addAttribute("post", edto);
            model.addAttribute("imgList", imageList);
            model.addAttribute("type",type);
            model.addAttribute("bucketName",bucket);
            model.addAttribute("regionName",region);
        }

    }


    @Override
    @Transactional
    public void writePost(List<MultipartFile> files, EducationDTO dto, int boardType) {
        Long eduNum = educationJPA.getAutoIncrementValue("pickyou", "education");

        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                if (file.getContentType().startsWith("image")) {
                    try {
                        String filePath = s3Service.uploadFile(file, "image/" + boardType + "/" + eduNum);
                        ImageDTO idto = new ImageDTO();
                        idto.setBoardNum(eduNum);
                        idto.setBoardType(boardType);
                        idto.setName(filePath);
                        imageJPA.save(idto.toImageEntity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        educationJPA.save(dto.toEducationEntity());
    }

    @Override
    @Transactional
    public void deletePost(Long boardNum, int boardType) {
        Optional<EducationEntity> education = educationJPA.findById(boardNum);
        if (education.isPresent()) {
                List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(boardType, boardNum);
                for (ImageEntity image : images) {
                    s3Service.deleteFile("image/"+boardType+"/"+boardNum+"/"+image.getName());
                }
            imageJPA.deleteAllByBoardTypeAndBoardNum(boardType, boardNum);
            educationJPA.deleteById(boardNum);

        }
    }

    @Override
    public int authCheck(Principal principal) {
        int auth=0;
        if(principal!=null){
            Optional<MemberEntity> opmem =memberJPA.findById(principal.getName());
            if(opmem.isPresent()){
                if(opmem.get().getAuth().contains("USER")){
                 auth=1;
                } else if (opmem.get().getAuth().contains("COMPANY")) {
                    auth=2;
                } else if (opmem.get().getAuth().contains("ADMIN")) {
                    auth=99;
                }
            }
        }
        return auth;
    }

    @Override
    public void update(List<MultipartFile> files, EducationDTO dto, int boardType) {
        Optional<EducationEntity> education = educationJPA.findById(dto.getId());
        if (education.isPresent()) {
            if (!CollectionUtils.isEmpty(files)) {
                List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(boardType, dto.getId());
                for (ImageEntity image : images) {
                    s3Service.deleteFile("image/" + boardType + "/" + dto.getId() + "/" + image.getName());
                }
                imageJPA.deleteAllByBoardTypeAndBoardNum(boardType, dto.getId());
                for (MultipartFile file : files) {
                    if (file.getContentType().startsWith("image")) {
                        try {
                            String filePath = s3Service.uploadFile(file, "image/" + boardType + "/" + dto.getId());
                            ImageDTO idto = new ImageDTO();
                            idto.setBoardNum(dto.getId());
                            idto.setBoardType(boardType);
                            idto.setName(filePath);
                            imageJPA.save(idto.toImageEntity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Date date = new Date();
            dto.setReg(date);
            educationJPA.save(dto.toEducationEntity());
        }
    }




@Override
public int favoriteCheck(PickDTO dto) {
    int favoritecheck = 0;
    PickID key = new PickID();
    key.setPicker(dto.getPicker());
    key.setTarget(dto.getTarget());
    Optional<PickEntity> pickcheck = pickJPA.findById(key);
    if (pickcheck.isPresent()) {
        pickJPA.deleteById(key);
    } else {
        pickJPA.save(dto.toPickEntity());
        favoritecheck = 1;
    }
    return favoritecheck;
}

    @Override
    public void updateReadCount(Long boardNum) {
        Optional<EducationEntity> oee = educationJPA.findById(boardNum);
        if(oee.isPresent()){
            EducationEntity ee=oee.get();
            ee.setReadCount(ee.getReadCount()+1);
            educationJPA.save(ee);
        }
    }

    @Override
    public boolean authCheck(Long boardNum, String id,int type) {
        boolean result = false;
        if(type==2) {
            Optional<EducationEntity> oee=educationJPA.findById(boardNum);
            if(oee.isPresent()){
                if(oee.get().getCompanyId().equals(id)){
                    result=true;
                }
            }
        } else if (type==5) {
            Optional<TeamResumeEntity> ote = teamResumeJPA.findById(boardNum);
                    if(ote.isPresent()){
                        if(ote.get().getMemberId().equals(id)){
                            result=true;
                        }
                    }
        } else if (type==6) {
            Optional<RecruitEntity> ore = recruitJPA.findById(boardNum);
            if(ore.isPresent()){
                if(ore.get().getMemberId().equals(id)){
                    result=true;
                }
            }
        } else if (type==7) {
            Optional<ContractEntity> oce = contractJPA.findById(boardNum);
            if(oce.isPresent()){
                if(oce.get().getMemberId().equals(id)){
                    result=true;
                } else if (oce.get().getCompanyId().equals(id)) {
                    result=true;
                }
            }
        }
        return result;
    }

    public String makeFolder(String uploadPath, int boardType, Long boardNum) {
    String folderPath = boardType + File.separator + boardNum;
    File uploadPathFoler = new File(uploadPath, folderPath);

    if (!uploadPathFoler.exists()) {
        uploadPathFoler.mkdirs();
    }
    return folderPath;
}

}
