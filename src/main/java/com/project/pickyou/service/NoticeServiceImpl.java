package com.project.pickyou.service;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.NoticeDTO;
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


    private final NoticeJPARepository noticeJPA;
    private final ImageJPARepository imageJPA;

    @Autowired
    public NoticeServiceImpl(NoticeJPARepository noticeJPA, ImageJPARepository imageJPA) {
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

    // 공지사항 인서트
    @Override
    public NoticeEntity noticeInsert(NoticeDTO dto) {
        return noticeJPA.save(dto.toNoticeEntity());
    }

    // 공지사항 이미지 인서트
    @Override
    public void saveImage(ImageDTO imageDTO, MultipartFile[] files) {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename(); // 원래 사진이름
                String fileName = generateUniqueFileName(originalFileName); // 아래에서 고유한 파일 이름 생성

                try {
                    // 파일 저장할 기본 경로
                    String baseDir = imgUploadPath;
                    // 공지사항 ID 설정과 타입 설정예정
                    int numtype = 1; // 공지사항 1번
                    String num = "1";
                    // 공지사항 ID를 기반으로 서브 폴더 생성
                    String subFolder = num + File.separator + imageDTO.getBoardNum(); // 파일 이름 설정하는것_번호 // num = 타입 1번이고 File.separator는 안에 파일 + 파일번호
                    Path directory = Paths.get(baseDir, subFolder); // 프로퍼티스 경로와 + 파일생성된 경로 합치기

                    // 서브 폴더가 존재하지 않으면 생성
                    if (!Files.exists(directory)) { // 해당 폴더를 확인하고 없으면 생성 확인
                        Files.createDirectories(directory);
                    }

                    // 파일 경로 설정
                    Path filePath = directory.resolve(fileName); // 경로속 uu아이디 이름 넣기
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    // 아래 5개는 사진 파일 DB에 저장하는것
                    // 파일 정보를 DB에 저장하거나 필요한 작업 수행
                    ImageEntity imageEntity = new ImageEntity();     // new 객체생성
                    imageEntity.setName(fileName);                   // 이름 설정
                    imageEntity.setBoardNum(imageDTO.getBoardNum()); // 이미지 DTO에서 boardNum을 가져와 설정
                    imageEntity.setBoardType(numtype);               // DB타입설정
                    imageJPA.save(imageEntity);            // 이미지 엔티티 저장

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
            File folder = new File(imgUploadPath + File.separator + 1 + File.separator + boardNum);
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

    // 공지사항 글 수정
    @Override
    public void update(List<MultipartFile> files, NoticeDTO dto) {
        Optional<NoticeEntity> notice = noticeJPA.findById(dto.getId());
        int check = 0;

        if (notice.isPresent()) {
            for (MultipartFile mf : files) {
                if (!mf.isEmpty()) {
                    if(check==0) {
                        File folder = new File(imgUploadPath + File.separator + 1 + File.separator + dto.getId());
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
                        imageJPA.deleteAllByBoardTypeAndBoardNum(1, dto.getId());
                        check++;
                    }
                    if (mf.getContentType().startsWith("image")) {
                        String originalName = mf.getOriginalFilename();
                        String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);
                        String folderPath = makeFolder(imgUploadPath, 1, dto.getId());
                        String uuid = UUID.randomUUID().toString();
                        String ext = originalName.substring(originalName.lastIndexOf("."));
                        String saveName = folderPath + File.separator + uuid + ext;


                        ImageDTO idto = new ImageDTO();
                        idto.setBoardNum(dto.getId());
                        idto.setBoardType(1);
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
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormat.format(currentDate);

        // 포맷팅된 문자열을 다시 Date 객체로 변환
        Date dateOnly = null;
        try {
            dateOnly = dateFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 포맷된 현재 날짜를 dto의 reg 속성에 설정

        dto.setReg(dateOnly);
        dto.setMemberId(notice.get().getMemberId());
        dto.setReadCount(notice.get().getReadCount());
        noticeJPA.save(dto.toNoticeEntity());
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