package com.project.pickyou.service;

import com.project.pickyou.dto.*;
import com.project.pickyou.entity.ImageEntity;
import com.project.pickyou.entity.PickEntity;
import com.project.pickyou.entity.PickID;
import com.project.pickyou.entity.RecruitEntity;
import com.project.pickyou.repository.ImageJPARepository;
import com.project.pickyou.repository.PickJPARepository;
import com.project.pickyou.repository.RecruitJPARepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
public class RecruitServiceImpl implements RecruitService{
    @Value("${img.upload.path}")
    private String imgUploadPath;


    private final RecruitJPARepository recruitJPA;
    private final ImageJPARepository imageJPA;
    private final PickJPARepository pickJPA;

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
    public void post(Model model, Long num,String sid) {
        Optional<RecruitEntity> post = recruitJPA.findById(num);
        RecruitDTO edto = new RecruitDTO();
        CompanyInfoDTO cidto = new CompanyInfoDTO();
        MemberDTO mdto = new MemberDTO();
        List<ImageEntity> imageList = Collections.emptyList();
        int favoritecheck = 0;

        if (post.isPresent()) {
            imageList = imageJPA.findByBoardTypeAndBoardNum(2, num);
            edto = post.get().toRecruitDTO();
            cidto = post.get().getMember().getCompanyInfo().toCompanyInfoDTO();
            mdto = post.get().getMember().toMemberDTO();
            PickID key = new PickID(sid, mdto.getId());
            edto.setContent(edto.getContent().replace("<br>","\r\n"));
            Optional<PickEntity> pickcheck = pickJPA.findById(key);
            if (pickcheck.isPresent()) {
                favoritecheck = 1;
            }
            model.addAttribute("favoritecheck", favoritecheck);
            model.addAttribute("member", mdto);
            model.addAttribute("company", cidto);
            model.addAttribute("post", edto);
            model.addAttribute("imgList", imageList);
        }

    }

    public String makeFolder(String uploadPath, int boardType, Long boardNum) {
        String folderPath = boardType + File.separator + boardNum;
        File uploadPathFoler = new File(uploadPath, folderPath);

        if (!uploadPathFoler.exists()) {
            uploadPathFoler.mkdirs();
        }
        return folderPath;
    }

    @Override
    @Transactional
    public void writePost(List<MultipartFile> files, RecruitDTO dto) {
        UUID uid = UUID.randomUUID();
        Long eduNum = recruitJPA.getAutoIncrementValue("pickyou", "education");
        recruitJPA.save(dto.toRecruitEntity());
        if (!files.isEmpty()) {
            for (MultipartFile mf : files) {
                if (mf.getContentType().startsWith("image")) {
                    String originalName = mf.getOriginalFilename();
                    String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);
                    System.out.println("================folderBoardnum" + eduNum);
                    String folderPath = makeFolder(imgUploadPath, 2, eduNum);
                    String uuid = UUID.randomUUID().toString();
                    String ext = originalName.substring(originalName.lastIndexOf("."));
                    String saveName = folderPath + File.separator + uuid + ext;
                    ImageDTO idto = new ImageDTO();
                    idto.setBoardNum(eduNum);
                    idto.setBoardType(2);
                    idto.setName(uuid + ext);
                    System.out.println("================= iamge before");
                    imageJPA.save(idto.toImageEntity());
                    System.out.println("================= iamge after");
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

    @Override
    @Transactional
    public void deletePost(Long boardNum) {
        System.out.println("=========deletenum1" + boardNum);
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
            imageJPA.deleteAllByBoardTypeAndBoardNum(2, boardNum);
            recruitJPA.deleteById(boardNum);

        }
    }

    @Override
    public void update(List<MultipartFile> files, RecruitDTO dto) {
        Optional<RecruitEntity> education = recruitJPA.findById(dto.getId());
        int check=0;

        if (education.isPresent()) {
            for (MultipartFile mf : files) {
                if(!mf.isEmpty()) {
                    if(check==0) {
                        File folder = new File(imgUploadPath + File.separator + 2 + File.separator + dto.getId());
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
                        imageJPA.deleteAllByBoardTypeAndBoardNum(2, dto.getId());
                        check++;
                    }

                    if (mf.getContentType().startsWith("image")) {
                        String originalName = mf.getOriginalFilename();
                        String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);
                        System.out.println("================folderBoardnum" + dto.getId());
                        String folderPath = makeFolder(imgUploadPath, 2, dto.getId());
                        String uuid = UUID.randomUUID().toString();
                        String ext = originalName.substring(originalName.lastIndexOf("."));
                        String saveName = folderPath + File.separator + uuid + ext;
                        ImageDTO idto = new ImageDTO();
                        idto.setBoardNum(dto.getId());
                        idto.setBoardType(2);
                        idto.setName(uuid + ext);
                        System.out.println("================= iamge before");
                        imageJPA.save(idto.toImageEntity());
                        System.out.println("================= iamge after");
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
        recruitJPA.save(dto.toRecruitEntity());
    }

    @Override
    public int favoriteCheck(PickDTO dto) {
        int favoritecheck =0;
        PickID key = new PickID();
        key.setPicker(dto.getPicker());
        key.setTarget(dto.getTarget());
        Optional<PickEntity> pickcheck = pickJPA.findById(key);
        if (pickcheck.isPresent()) {
            pickJPA.deleteById(key);
        } else {
            pickJPA.save(dto.toPickEntity());
            favoritecheck=1;
        }
        return favoritecheck;
    }
}
