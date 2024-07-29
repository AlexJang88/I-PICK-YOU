package com.project.pickyou.service;

import com.project.pickyou.dto.FoodMapDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.PointDTO;
import com.project.pickyou.entity.EducationEntity;
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

@Service
@RequiredArgsConstructor
public class FoodMapServiceImpl implements FoodMapService {


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Service s3Service;

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
    public void saveImage(FoodMapDTO dto, List<MultipartFile> files) {
        Long foodMapNum = foodMapJPA.getAutoIncrementValue("pickyou", "food_map");

        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                if (file.getContentType().startsWith("image")) {
                    try {
                        String filePath = s3Service.uploadFile(file, "image/" + 4 + "/" + foodMapNum);
                        ImageDTO idto = new ImageDTO();
                        idto.setBoardNum(foodMapNum);
                        idto.setBoardType(4);
                        idto.setName(filePath);
                        imageJPA.save(idto.toImageEntity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        dto.setRef(foodMapNum.intValue());
        foodMapJPA.save(dto.toFood_MapEntity());
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
        model.addAttribute("bucketName",bucket);
        model.addAttribute("regionName",region);

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
            List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(4, boardNum);
            for (ImageEntity image : images) {
                s3Service.deleteFile("image/"+4+"/"+boardNum+"/"+image.getName());
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
    @Transactional
    public void update(List<MultipartFile> files, FoodMapDTO dto) {
        Optional<FoodMapEntity> foodMap = foodMapJPA.findById(dto.getId());
        if (foodMap.isPresent()) {
            if (!CollectionUtils.isEmpty(files)) {
                List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(4, dto.getId());
                for (ImageEntity image : images) {
                    s3Service.deleteFile("image/" + 4 + "/" + dto.getId() + "/" + image.getName());
                }
                imageJPA.deleteAllByBoardTypeAndBoardNum(4, dto.getId());
                for (MultipartFile file : files) {
                    if (file.getContentType().startsWith("image")) {
                        try {
                            String filePath = s3Service.uploadFile(file, "image/" + 4 + "/" + dto.getId());
                            ImageDTO idto = new ImageDTO();
                            idto.setBoardNum(dto.getId());
                            idto.setBoardType(4);
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
            foodMapJPA.save(dto.toFood_MapEntity());
        }

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
    public void refInsert(FoodMapDTO dto, List<MultipartFile> files) {
        Long foodMapNum = foodMapJPA.getAutoIncrementValue("pickyou", "food_map");

        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                if (file.getContentType().startsWith("image")) {
                    try {
                        String filePath = s3Service.uploadFile(file, "image/" + 4 + "/" + foodMapNum);
                        ImageDTO idto = new ImageDTO();
                        idto.setBoardNum(foodMapNum);
                        idto.setBoardType(4);
                        idto.setName(filePath);
                        imageJPA.save(idto.toImageEntity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        dto.setReply(foodMapNum.intValue());
        foodMapJPA.save(dto.toFood_MapEntity());
    }

    // 푸드맵 대댓글 인서트
    @Override
    public void replyInsert(FoodMapDTO dto, List<MultipartFile> files) {
        int reply = dto.getReply();
        Long foodMapNum = foodMapJPA.getAutoIncrementValue("pickyou", "food_map");

        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                if (file.getContentType().startsWith("image")) {
                    try {
                        String filePath = s3Service.uploadFile(file, "image/" + 4 + "/" + foodMapNum);
                        ImageDTO idto = new ImageDTO();
                        idto.setBoardNum(foodMapNum);
                        idto.setBoardType(4);
                        idto.setName(filePath);
                        imageJPA.save(idto.toImageEntity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        dto.setReply(reply);
        foodMapJPA.save(dto.toFood_MapEntity());
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
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    } // 이미지 넣기















}
