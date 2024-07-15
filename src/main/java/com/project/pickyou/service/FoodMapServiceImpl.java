package com.project.pickyou.service;

import com.project.pickyou.dto.FoodMapDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.PointDTO;
import com.project.pickyou.entity.FoodMapEntity;
import com.project.pickyou.entity.ImageEntity;
import com.project.pickyou.entity.NoticeEntity;
import com.project.pickyou.repository.FoodMapJPARepository;
import com.project.pickyou.repository.ImageJPARepository;
import com.project.pickyou.repository.PointJPARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
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

@Service
@RequiredArgsConstructor
public class FoodMapServiceImpl implements FoodMapService {

    @Value("${FDimg.upload.path}")
    private String imgUploadPath; // 프로퍼티스에서 정한 결로를, 웹 컨피그에서 해당 경로를 지정해서 클래스안에 설정


    private final FoodMapJPARepository foodMapJPA;
    private final ImageJPARepository imageJPA;
    private final PointJPARepository pointJPA;

    // 푸드맵 리스트 가져오기, 페이징 처리
    @Override
    public void AllPosts(Model model, int pageNum) {
        int pageSize = 10;
        Long longCount = foodMapJPA.fmCount();
        int count = longCount.intValue();

        Sort sort = Sort.by(Sort.Order.asc("status"), Sort.Order.desc("reg"));


        Page<FoodMapEntity> page = foodMapJPA.foodMapList(PageRequest.of(pageNum - 1, pageSize, sort));

        List<FoodMapEntity> posts = page.getContent();

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

    // 푸드맵 인서트
    @Override
    public FoodMapEntity foodMapInsert(FoodMapDTO dto) {
        Long ref = foodMapJPA.getAutoIncrementValue("pickyou", "food_map");
        int refInt = ref.intValue();
        dto.setRef(refInt);
        dto.setStatus(2);
        return foodMapJPA.save(dto.toFood_MapEntity());
    }

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
                    int numtype = 4; // 푸드맵 4번
                    String num = "4";
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

    // 푸드맵 상세정보
    @Override
    public void foodMapInfo(int ref, Model model) {
        long id = ref;

        Optional<FoodMapEntity> optional =  foodMapJPA.findByIdAndRef(id, ref);
        if (optional.isPresent()) {
            FoodMapEntity entity = optional.get();
            model.addAttribute("foodMapInfo", entity);
        }


/*
        if (!fmInfoList.isEmpty()) {
            FoodMapEntity firstItem = fmInfoList.get(0);
            model.addAttribute("fmInfoGet0", firstItem);
        }*/

        List<FoodMapEntity> fmInfoList = foodMapJPA.findByRef(ref);
        model.addAttribute("fmInfo", fmInfoList);

        fmInfoList.sort(Comparator.comparing(FoodMapEntity::getReply));

        if (!fmInfoList.isEmpty()) {
            List<FoodMapEntity> fmInfoExcludingFirst = fmInfoList.subList(1, fmInfoList.size()); // 첫 번째 값을 제외한 나머지 엔터티 리스트를 가져옵니다.
            model.addAttribute("refList", fmInfoExcludingFirst); // 모델에 첫 번째 값을 제외한 엔터티 리스트를 추가합니다.

            List<ImageEntity> allImagesRef = new ArrayList<>();
            for (FoodMapEntity entity : fmInfoExcludingFirst) {

                List<ImageEntity> imagesRef = imageJPA.findByBoardTypeAndBoardNum(4, entity.getId());
                allImagesRef.addAll(imagesRef);
            }
            model.addAttribute("imagesRef", allImagesRef); // 모델에 모든 이미지를 추가합니다.
        }

    }

    // 푸드맵 이미지 가져오기
    @Override
    public void foodMapImage(Long boardNum, int boardType, Model model) {
        // 푸드맵 글번호의 이미지 가져오기
        List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(boardType, boardNum);
        model.addAttribute("images", images);
    }

    // 푸드맵 글 삭제
    @Override
    @Transactional
    public void foodMapDelete(Long boardNum) {
        Optional<FoodMapEntity> foodMap = foodMapJPA.findById(boardNum);
        if (foodMap.isPresent()) {
            File folder = new File(imgUploadPath + File.separator + 4 + File.separator + boardNum);
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
            imageJPA.deleteAllByBoardTypeAndBoardNum(4, boardNum);
            foodMapJPA.deleteById(boardNum);
        }
    }

    // 푸드맵 글 수정
    @Override
    public void foodMapUpdate(Model model, Long num) {
        int ref = num.intValue();
        Optional<FoodMapEntity> post = foodMapJPA.findByIdAndRef(num, ref);
        FoodMapDTO Fdto = new FoodMapDTO();
        List<ImageEntity> imageList = Collections.emptyList();

        if (post.isPresent()) {
            imageList = imageJPA.findByBoardTypeAndBoardNum(4, num);
            Fdto = post.get().toFood_MapDTO();
            Fdto.setContent(Fdto.getContent().replace("<br>","\r\n"));

            model.addAttribute("Fdto", Fdto);
            model.addAttribute("imgList", imageList);
        }
    }

    // 푸드맵 글 수정
    @Override
    public void update(List<MultipartFile> files, FoodMapDTO dto) {
        Optional<FoodMapEntity> foodMap = foodMapJPA.findById(dto.getId());
        int check = 0;

        if (foodMap.isPresent()) {
            for (MultipartFile mf : files) {
                if (!mf.isEmpty()) {
                    if(check==0) {
                        File folder = new File(imgUploadPath + File.separator + 4 + File.separator + dto.getId());
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
                        imageJPA.deleteAllByBoardTypeAndBoardNum(4, dto.getId());
                        check++;
                    }
                    if (mf.getContentType().startsWith("image")) {
                        String originalName = mf.getOriginalFilename();
                        String fileName = originalName.substring(originalName.lastIndexOf("//") + 4);
                        String folderPath = makeFolder(imgUploadPath, 4, dto.getId());
                        String uuid = UUID.randomUUID().toString();
                        String ext = originalName.substring(originalName.lastIndexOf("."));
                        String saveName = folderPath + File.separator + uuid + ext;

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
                        dto.setMemberId(foodMap.get().getMemberId());
                        dto.setReadCount(foodMap.get().getReadCount());
                        dto.setMap(foodMap.get().getMap());
                        dto.setRef(foodMap.get().getRef());

                        ImageDTO idto = new ImageDTO();
                        idto.setBoardNum(dto.getId());
                        idto.setBoardType(4);
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
        foodMapJPA.save(dto.toFood_MapEntity());
    }

    // 푸드맵 조회수 증가
    @Override
    public void foodMapCnt(Long id, int ref, Model model) {
        Optional<FoodMapEntity> optional = foodMapJPA.findByIdAndRef(id, ref);
        FoodMapDTO dto = new FoodMapDTO();

        if (optional.isPresent()) {
            FoodMapEntity foodMapEntity = optional.get();
            dto = optional.get().toFood_MapDTO();

            dto.setId(dto.getId());
            dto.setContent(dto.getContent());
            dto.setMemberId(dto.getMemberId());
            dto.setRef(dto.getRef());
            dto.setReg(dto.getReg());
            dto.setTitle(dto.getTitle());
            dto.setMap(dto.getMap());
            dto.setReply(dto.getReply());
            dto.setReadCount(dto.getReadCount()+1);

            // 업데이트
            foodMapJPA.save(dto.toFood_MapEntity());
        }
    }

    // 푸드맵 댓글 인서트
    @Override
    public FoodMapEntity refInsert(FoodMapDTO dto, int ref) {
        Long findId = foodMapJPA.getAutoIncrementValue("pickyou", "food_map");
        findId.intValue();

        List<FoodMapEntity> FoodMapEntityList = foodMapJPA.findByRef(ref);

        FoodMapEntity foodMapEntity = FoodMapEntityList.get(0);

        dto.setTitle(foodMapEntity.getTitle());
        dto.setMap(foodMapEntity.getMap());
        dto.setRef(foodMapEntity.getRef());
        dto.setReply(findId.intValue());

        return foodMapJPA.save(dto.toFood_MapEntity());
    }

    // 푸드맵 대댓글 인서트
    @Override
    public FoodMapEntity replyInsert(FoodMapDTO dto) {
        /*List<FoodMapEntity> FoodMapEntityList = foodMapJPA.findByRef(ref);

        FoodMapEntity foodMapEntity = FoodMapEntityList.get(0);*/

        /*dto.setRef(dto.getRef());
        dto.setTitle(dto.getTitle());
        dto.setMap(dto.getMap());*/

        return foodMapJPA.save(dto.toFood_MapEntity());
    }

    // 푸드맵 인증글 포인트 인서트
    @Override
    public void foodMapPointInsert(PointDTO dto, FoodMapDTO FMdto) {
        System.out.println(FMdto);

        FMdto.setId(FMdto.getId());
        FMdto.setStatus(1);
        FMdto.setMap(FMdto.getMap());
        FMdto.setRef(FMdto.getRef());
        FMdto.setTitle(FMdto.getTitle());
        FMdto.setContent(FMdto.getContent());
        FMdto.setReply(FMdto.getReply());
        FMdto.setReadCount(FMdto.getReadCount());
        FMdto.setReg(FMdto.getReg());
        FMdto.setMemberId(FMdto.getMemberId());

        foodMapJPA.save(FMdto.toFood_MapEntity());
        // 포인트 인서트
        pointJPA.save(dto.toPointEntity());
    }

    public String makeFolder(String uploadPath, int boardType, Long boardNum) {
        String folderPath = boardType + File.separator + boardNum;
        File uploadPathFoler = new File(uploadPath, folderPath);

        if (!uploadPathFoler.exists()) {
            uploadPathFoler.mkdirs();
        }
        return folderPath;
    }

    // 실제 가져온 파일이름말고 내가 지정한 파일이름으로 저장
    private String generateUniqueFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFileName);
        return uuid + "." + extension;
    }

    private String getFileExtension(String fileName) { // 확장자 추출
        return fileName.substring(fileName.lastIndexOf(".") + 4);
    } // 이미지 넣기















}
