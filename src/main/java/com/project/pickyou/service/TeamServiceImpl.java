package com.project.pickyou.service;

import com.project.pickyou.dto.*;
import com.project.pickyou.entity.*;
import com.project.pickyou.repository.ImageJPARepository;
import com.project.pickyou.repository.MemberJPARepository;
import com.project.pickyou.repository.PickJPARepository;
import com.project.pickyou.repository.TeamResumeJPARepository;
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
public class TeamServiceImpl implements TeamService{
    @Value("${img.upload.path}")
    private String imgUploadPath;

    @Value("${profile.upload.path}")
    private String profileUploadPath;

    private final TeamResumeJPARepository teamJPA;
    private final ImageJPARepository imageJPA;
    private final PickJPARepository pickJPA;
    private final MemberJPARepository memberJPA;

    @Override
    public void AllPosts(Model model, int pageNum,String sid) {
        int veil=0;
        int pageSize = 9;
        Long longCount = teamJPA.countByStatus(1);
        int count = longCount.intValue();

        int check = 0;

        Long longcheck=teamJPA.countByMemberId(sid);
        check= longcheck.intValue();

        if(check>0){
            veil=1;
        }
        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<TeamResumeEntity> page = teamJPA.findAllByStatus(1,PageRequest.of(pageNum - 1, pageSize, sort));
        //  로그인 처리후 사용할 코드
        //   Page<TeamResumeEntity> page = teamJPA.findAllByStatus(1, PageRequest.of(pageNum - 1, pageSize, sort));
        List<TeamResumeEntity> posts = page.getContent();
        model.addAttribute("veil",veil);
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
    public void post(Model model,Long num,String sid,int boardType) {
        int type=0;
        int auth=0;
        int check=0;
        Optional<TeamResumeEntity> post = teamJPA.findById(num);
        Optional<MemberEntity> member =memberJPA.findById(sid);
        TeamResumeDTO tdto = new TeamResumeDTO();
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
            if(post.get().getMemberId().equals(sid)){
                type=1;
            }
            imageList = imageJPA.findByBoardTypeAndBoardNum(boardType, num);
            tdto = post.get().toTeam_ResumeDTO();
            mdto = post.get().getMember().toMemberDTO();
            PickID key = new PickID(sid, mdto.getId());
            Optional<PickEntity> pickcheck = pickJPA.findById(key);
            if (pickcheck.isPresent()) {
                favoritecheck = 1;
            }
            model.addAttribute("check",check);
            model.addAttribute("auth",auth);
            model.addAttribute("type",type);
            model.addAttribute("favoritecheck", favoritecheck);
            model.addAttribute("member", mdto);
            model.addAttribute("post", tdto);
            model.addAttribute("imgList", imageList);
            model.addAttribute("to",sid);
            model.addAttribute("id",sid);

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
    public void writePost(MultipartFile profile,List<MultipartFile> files, TeamResumeDTO dto,int boardType) {

        Long teamNum = teamJPA.getAutoIncrementValue("pickyou", "team_resume");

        if(!profile.isEmpty()) {
           String profileName= profileUpload(profile, profileUploadPath);
           dto.setProfile(profileName);
        }
        teamJPA.save(dto.toTeam_ResumeEntity());
        if(!files.isEmpty()) {
            filesUpload(files, boardType, teamNum, imgUploadPath);
        }

    }
    public String profileUpload(MultipartFile file,String uploadPath){
        String profileName="default.jpeg";

        if (file.getContentType().startsWith("image")) {
            String originalName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String ext = originalName.substring(originalName.lastIndexOf("."));
            profileName=uuid+ext;
            String saveName = uploadPath + File.separator + uuid + ext;
            Path savePath = Paths.get(saveName);
            try {
                file.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return profileName;
    }
    public void filesUpload(List<MultipartFile> files,int boardType,Long BoardNum,String uploadPath){
        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile mf : files) {
                if (mf.getContentType().startsWith("image")) {
                    String originalName = mf.getOriginalFilename();
                    String folderPath = makeFolder(uploadPath, boardType, BoardNum);
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


    @Override
    public void update(MultipartFile profile,List<MultipartFile> files, TeamResumeDTO dto,int boardType) {
        Optional<TeamResumeEntity> team = teamJPA.findById(dto.getId());
            if (team.isPresent()) {
                    if(!CollectionUtils.isEmpty(files)){
                            File folder = new File(imgUploadPath + File.separator + boardType + File.separator + dto.getId());
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
                            imageJPA.deleteAllByBoardTypeAndBoardNum(boardType, dto.getId());
                        filesUpload(files,boardType,dto.getId(),imgUploadPath);
                    }
            }
            if(!profile.isEmpty()) {
                File file = new File(profileUploadPath+File.separator+dto.getProfile());
                try {
                    if(file.exists()){
                        file.delete();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                String profileName=profileUpload(profile, profileUploadPath);
                dto.setProfile(profileName);
            }
        teamJPA.save(dto.toTeam_ResumeEntity());
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

    @Override
    public void exposure(TeamResumeDTO dto) {
        if(dto.getStatus()==1){
            dto.setStatus(0);
            teamJPA.save(dto.toTeam_ResumeEntity());
        }else{
            dto.setStatus(1);
            teamJPA.save(dto.toTeam_ResumeEntity());
        }
    }

    @Override
    public void MyPosts(Model model , String id,int boardType) {
        Optional<TeamResumeEntity> post = teamJPA.findAllByMemberId(id);
        TeamResumeDTO tdto = new TeamResumeDTO();
        MemberDTO mdto = new MemberDTO();
        List<ImageEntity> imageList = Collections.emptyList();
        if (post.isPresent()) {
            imageList = imageJPA.findByBoardTypeAndBoardNum(boardType, post.get().getId());

            tdto = post.get().toTeam_ResumeDTO();
            mdto = post.get().getMember().toMemberDTO();
            model.addAttribute("member", mdto);
            model.addAttribute("post", post.get());
            model.addAttribute("imgList", imageList);
            model.addAttribute("check", 1);
        }else{
            model.addAttribute("check", 0);
            model.addAttribute("memberId",id);
        }


    }

    @Override
    public boolean checkMyPost(String id) {
        boolean result=false;
            Optional<TeamResumeEntity> check=teamJPA.findAllByMemberId(id);
            if(check.isPresent()){
                result=true;
            }
        return result;
    }
}
