package com.project.pickyou.service;

import com.project.pickyou.dto.EducationDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.NoticeDTO;
import com.project.pickyou.entity.EducationEntity;
import com.project.pickyou.entity.ImageEntity;
import com.project.pickyou.entity.NoticeEntity;
import com.project.pickyou.repository.ImageJPARepository;
import com.project.pickyou.repository.NoticeJPARepository;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class NoticeServiceImpl implements NoticeService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Service s3Service;

    private final NoticeJPARepository noticeJPA;
    private final ImageJPARepository imageJPA;

    @Autowired
    public NoticeServiceImpl(S3Service s3Service, NoticeJPARepository noticeJPA, ImageJPARepository imageJPA) {
        this.s3Service = s3Service;
        this.noticeJPA = noticeJPA;
        this.imageJPA = imageJPA;
    }

    // 공지사항 리스트 가져오기, 페이징 처리
    @Transactional
    @Override
    public void AllPosts(Model model, int pageNum) {

        int pageSize = 10;
        Long longCount = noticeJPA.count();
        int count = longCount.intValue();

        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<NoticeEntity> page = noticeJPA.findAll(PageRequest.of(pageNum - 1, pageSize, sort));

        List<NoticeEntity> posts = page.getContent();

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
/*

    // 공지사항 인서트
    @Override
    public NoticeEntity noticeInsert(NoticeDTO dto) {
        return noticeJPA.save(dto.toNoticeEntity());
    }
*/

    // 공지사항 이미지 인서트
    @Override
    @Transactional
    public void saveImage(NoticeDTO dto, List<MultipartFile> files) {
        Long noticeNum = noticeJPA.getAutoIncrementValue("pickyou", "notice");

        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                if (file.getContentType().startsWith("image")) {
                    try {
                        String filePath = s3Service.uploadFile(file, "image/" + 1 + "/" + noticeNum);
                        ImageDTO idto = new ImageDTO();     // new 객체생성
                        idto.setName(filePath);                   // 이름 설정
                        idto.setBoardNum(noticeNum); // 이미지 DTO에서 boardNum을 가져와 설정
                        idto.setBoardType(1);               // DB타입설정
                        imageJPA.save(idto.toImageEntity());            // 이미지 엔티티 저장

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        noticeJPA.save(dto.toNoticeEntity());
    }

    // 실제 가져온 파일이름말고 내가 지정한 파일이름으로 저장
    private String generateUniqueFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFileName);
        return uuid + "." + extension;
    }

    private String getFileExtension(String fileName) { // 확장자 추출
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    } // 이미지 넣기

    // 공지사항 이미지 가져오기
    public void noticeImage(Long boardNum, int boardType, Model model) {
        // 공지사항 글번호의 이미지 가져오기
        List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(boardType,boardNum);
        model.addAttribute("images", images);
        model.addAttribute("bucketName",bucket);
        model.addAttribute("regionName",region);
    }

    // 공지사항 조회수 증가
    @Override
    public void noticeCnt(Long id, Model model) {
        Optional<NoticeEntity> optional = noticeJPA.findById(id);
        NoticeDTO dto = new NoticeDTO();

        if (optional.isPresent()) {
            NoticeEntity noticeEntity = optional.get();
            dto = optional.get().toNoticeDTO();

            dto.setId(dto.getId());
            dto.setMemberId(dto.getMemberId());
            dto.setTitle(dto.getTitle());
            dto.setContent(dto.getContent());
            dto.setReadCount(dto.getReadCount() + 1);
            dto.setReg(dto.getReg());

            // 업데이트
            noticeJPA.save(dto.toNoticeEntity());
        }
    }

    // 메인에 공지사항 최신글 1개
    @Override
    public void mainNotice(Model model) {
        Optional<NoticeEntity> optional = noticeJPA.findTopByOrderByRegDesc();
        if (optional.isPresent()) {
            NoticeEntity noticeEntity = optional.get();
            model.addAttribute("notice", noticeEntity);
        }
    }

    // 공지사항 글번호의 내용 가져오기
    @Override
    public void noticeInfo(Long id, Model model) {
        // 공지사항 글번호의 내용 가져오기
        Optional<NoticeEntity> optional = noticeJPA.findById(id);

        if (optional.isPresent()) {
            NoticeEntity noticeEntity = optional.get();
            model.addAttribute("noticeInfo", noticeEntity);
        }
    }

    // 공지사항 글 삭제
    @Override
    @Transactional
    public void noticeDelete(Long boardNum) {
        Optional<NoticeEntity> notice = noticeJPA.findById(boardNum);
        if (notice.isPresent()) {
            List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(1, boardNum);
            for (ImageEntity image : images) {
                s3Service.deleteFile("image/"+1+"/"+boardNum+"/"+image.getName());
            }
            imageJPA.deleteAllByBoardTypeAndBoardNum(1, boardNum);
            noticeJPA.deleteById(boardNum);
        }
    }

    // 공지사항 글 수정
    @Override
    public void noticeUpdate(Model model, Long num) {
        Optional<NoticeEntity> post = noticeJPA.findById(num);
        NoticeDTO Ndto = new NoticeDTO();
        List<ImageEntity> imageList = Collections.emptyList();

        if (post.isPresent()) {
            imageList = imageJPA.findByBoardTypeAndBoardNum(1,num);
            Ndto = post.get().toNoticeDTO();
            Ndto.setContent(Ndto.getContent().replace("<br>","\r\n"));

            model.addAttribute("Ndto", Ndto);
            model.addAttribute("imgList", imageList);
        }
    }

    ///////////////////////////
    // 공지사항 글 수정
    @Override
    public void update(List<MultipartFile> files, NoticeDTO dto) {
        Optional<NoticeEntity> notice = noticeJPA.findById(dto.getId());
        if (notice.isPresent()) {
            if (!CollectionUtils.isEmpty(files)) {
                List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(1, dto.getId());
                for (ImageEntity image : images) {
                    s3Service.deleteFile("image/" + 1 + "/" + dto.getId() + "/" + image.getName());
                }
                imageJPA.deleteAllByBoardTypeAndBoardNum(1, dto.getId());
                for (MultipartFile file : files) {
                    if (file.getContentType().startsWith("image")) {
                        try {
                            String filePath = s3Service.uploadFile(file, "image/" + 1 + "/" + dto.getId());
                            ImageDTO idto = new ImageDTO();
                            idto.setBoardNum(dto.getId());
                            idto.setBoardType(1);
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
            noticeJPA.save(dto.toNoticeEntity());
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
}