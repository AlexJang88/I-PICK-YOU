package com.project.pickyou.service;

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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecruitServiceImpl implements RecruitService {
    @Value("${img.upload.path}")
    private String imgUploadPath;
    private final RecruitJPARepository recruitJPA;
    private final RecruitStateJPARepository recruitStateJPA;
    private final ImageJPARepository imageJPA;
    private final PickJPARepository pickJPA;
    private final ResumeJPARepository resumeJPA;
    private final RecruitDetailJPARepository recruitDetailJPA;

    @Override
    public void AllPosts(Model model, int pageNum) {
        int pageSize = 10;
        Long longCount = recruitJPA.count();
        int count = longCount.intValue();

        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<RecruitEntity> page = recruitJPA.findAll(PageRequest.of(pageNum - 1, pageSize, sort));

        List<RecruitEntity> posts = page.getContent();

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

    @Override
    public void post(Model model, Long num, String sid) {
        Optional<RecruitEntity> post = recruitJPA.findById(num);
        RecruitDTO edto = new RecruitDTO();
        CompanyInfoDTO cidto = new CompanyInfoDTO();
        MemberDTO mdto = new MemberDTO();
        List<ImageEntity> imageList = Collections.emptyList();
        String gender = "성별 무관";
        String type = "일반";
        int favoritecheck = 0;

        if (post.isPresent()) {
            imageList = imageJPA.findByBoardTypeAndBoardNum(2, num);
            edto = post.get().toRecruitDTO();
            cidto = post.get().getMember().getCompanyInfo().toCompanyInfoDTO();
            mdto = post.get().getMember().toMemberDTO();
            PickID key = new PickID(sid, mdto.getId());
            edto.setContent(edto.getContent().replace("<br>", "\r\n"));
            Optional<PickEntity> pickcheck = pickJPA.findById(key);
            if (pickcheck.isPresent()) {
                favoritecheck = 1;
            }
            if (post.get().getRecruitDetail().getGender() == 2) {
                gender = "남성";
            } else if (post.get().getRecruitDetail().getGender() == 3) {
                gender = "여성";
            }
            if (post.get().getStatus() == 2) {
                type = "긴급";
            }
            model.addAttribute("type", type);
            model.addAttribute("gender", gender);
            model.addAttribute("favoritecheck", favoritecheck);
            model.addAttribute("member", mdto);
            model.addAttribute("company", cidto);
            model.addAttribute("post", post.get());
            model.addAttribute("imgList", imageList);
        }

    }


    @Override
    @Transactional
    public void writePost(List<MultipartFile> files, RecruitDTO rdto, RecruitDetailDTO rddto, int BoardType) {

        Long recruitNum = recruitJPA.getAutoIncrementValue("pickyou", "recruit");
        rddto.setRecruitId(recruitNum);

        recruitJPA.save(rdto.toRecruitEntity());
        recruitDetailJPA.save(rddto.toRecruitDetailEntity());

        if (!CollectionUtils.isEmpty(files)) {
            filesUpload(files, BoardType, recruitNum, imgUploadPath);
        }

    }

    @Override
    @Transactional
    public void deletePost(Long boardNum,int boardType) {
        Optional<RecruitEntity> education = recruitJPA.findById(boardNum);
        if (education.isPresent()) {
            File folder = new File(imgUploadPath + File.separator + 2 + File.separator + boardNum);
            try {
                if (folder.exists()) {
                    FileUtils.cleanDirectory(folder);
                }
                if (folder.isDirectory()) {
                    folder.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageJPA.deleteAllByBoardTypeAndBoardNum(boardType, boardNum);
            recruitJPA.deleteById(boardNum);

        }
    }

    @Override
    public void update(List<MultipartFile> files, RecruitDTO rdto,RecruitDetailDTO rddto,int boardType) {
        Optional<RecruitEntity> recruit = recruitJPA.findById(rdto.getId());
        if (recruit.isPresent()) {
            if (!CollectionUtils.isEmpty(files)) {
                for (MultipartFile mf : files) {
                    File folder = new File(imgUploadPath + File.separator + 2 + File.separator + rdto.getId());
                    try {
                        if (folder.exists()) {
                            FileUtils.cleanDirectory(folder);
                        }
                        if (folder.isDirectory()) {
                            folder.delete();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageJPA.deleteAllByBoardTypeAndBoardNum(boardType, rdto.getId());
                   filesUpload(files,boardType,rdto.getId(),imgUploadPath);
                }
            }
        }
        recruitJPA.save(rdto.toRecruitEntity());
        recruitDetailJPA.save(rddto.toRecruitDetailEntity());
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
    public int recruit(Long boardNum, String id) {
        int result = 0;
        Optional<ResumeEntity> resume = resumeJPA.findByMemberIdAndRegType(id, 1);
        if (resume.isPresent()) {
            Optional<RecruitStateEntity> state = recruitStateJPA.findByRecruitIdAndMemberId(boardNum, id);
            if (state.isEmpty()) {
                RecruitStateDTO rsdto = new RecruitStateDTO();
                rsdto.setMemberId(id);
                rsdto.setRecruitId(boardNum);
                recruitStateJPA.save(rsdto.toRecruitStateEntity());
                result = 1;
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
    public void filesUpload(List<MultipartFile> files, int boardType, Long BoardNum, String uploadPath) {
        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile mf : files) {
                if (mf.getContentType().startsWith("image")) {
                    String originalName = mf.getOriginalFilename();
                    String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);
                    String folderPath = makeFolder(imgUploadPath, boardType, BoardNum);
                    String uuid = UUID.randomUUID().toString();
                    String ext = originalName.substring(originalName.lastIndexOf("."));
                    String saveName = folderPath + File.separator + uuid + ext;
                    ImageDTO idto = new ImageDTO();
                    idto.setBoardNum(BoardNum);
                    idto.setBoardType(boardType);
                    idto.setName(uuid + ext);
                    imageJPA.save(idto.toImageEntity());
                    Path savePath = Paths.get(imgUploadPath, saveName);
                    try {
                        mf.transferTo(savePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
