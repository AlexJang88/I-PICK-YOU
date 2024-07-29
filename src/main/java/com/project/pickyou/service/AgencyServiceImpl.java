package com.project.pickyou.service;


import com.project.pickyou.dto.AgencyDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.entity.AgencyEntity;
import com.project.pickyou.entity.ImageEntity;
import com.project.pickyou.entity.TrainningEntity;
import com.project.pickyou.repository.AgencyJPARepository;
import com.project.pickyou.repository.ImageJPARepository;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgencyServiceImpl implements AgencyService{


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    private final AgencyJPARepository agencyJPARepository;
    private final ImageJPARepository imageJPARepository;
    private final S3Service s3Service;

    @Override
    public void agencyCount(Long agencynum) { //조회수 증가
        Optional<AgencyEntity> optional = agencyJPARepository.findById(agencynum);

        if (optional.isPresent()) {
            AgencyEntity agcEntity = optional.get();
            agcEntity.setReadCount(agcEntity.getReadCount()+1);
            agencyJPARepository.save(agcEntity);
        }

    }

    //소개소 메인
    @Override
    public void agencymain(Model model, int pageNum) {

        int pageSize = 9;
        Long longCount = agencyJPARepository.count();
        int count = longCount.intValue(); //롱타입을 인트로 변환

        Sort sort = Sort.by(Sort.Order.desc("reg"));  //내림차순
        Page<AgencyEntity> page = agencyJPARepository.findAll(PageRequest.of(pageNum - 1, pageSize, sort));

        List<AgencyEntity> agencyEntityList = page.getContent();

        model.addAttribute("agencyEntityList", agencyEntityList);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum - 1) / 10 * 10 + 1;
        int pageBlock = 10;  // 페이징(이전/다음)을 몇 개 단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }

        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
        model.addAttribute("bucketName",bucket);
        model.addAttribute("regionName",region);


    }


    //소개소 상세
    @Override
    public void agencyDetails(Model model, Long agencynum) {
        Optional<AgencyEntity> AgencyList = agencyJPARepository.findById(agencynum);
        List<ImageEntity> images = imageJPARepository.findByBoardTypeAndBoardNum(7,agencynum);

        model.addAttribute("images", images);
        model.addAttribute("AgencyList",AgencyList);
        model.addAttribute("bucketName",bucket);  //아마존 경로
        model.addAttribute("regionName",region); //아마존 경로
    }


    //소개소 신규 내용 추가하기
    @Override
    public void saveImage(AgencyDTO agencyDTO, MultipartFile[] files) {
        Long agencyNum = agencyJPARepository.getAutoIncrementValue("pickyou", "agency");

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                try {
                    // 훈련소 ID 설정 과 타입 설정예정
                    int numtype = 7;  //소개소는 3임
                    // 파일 저장할 기본 경로
                    String baseDir = s3Service.uploadFile(file, "image/" + numtype + "/" + agencyNum); //아마존


                    /*아래5개는 사진 파일 db에 저장하는것*/
                    // 파일 정보를 DB에 저장하거나 필요한 작업 수행
                    ImageEntity imageEntity = new ImageEntity();  //new객체생성
                    imageEntity.setName(baseDir);                   //이름설정
                    imageEntity.setBoardNum(agencyNum); // 이미지 DTO에서 boardNum을 가져와 설정
                    imageEntity.setBoardType(numtype);           //db타입설정
                    imageJPARepository.save(imageEntity); // 이미지 엔티티 저장
                } catch (IOException e) {
                    e.printStackTrace();
                    // 파일 저장 실패 시 예외 처리
                }
            }
        }
        agencyJPARepository.save(agencyDTO.toAgencyEntity());//인력사무소 내용저장
    }

    private String generateUniqueFileName(String originalFileName) {   //저장된 이미지 이름 변경
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFileName);
        return uuid + "." + extension;
    }

    private String getFileExtension(String fileName) {   //확장자 추출

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }






    //소개소 내용 및 이미지 지우기
    @Override
    @Transactional
    public void deleteAgencyImg(Long agencynum) {
        int boardType= 7;
        Optional<AgencyEntity> agency = agencyJPARepository.findById(agencynum);

        if (agency.isPresent()) {
            List<ImageEntity> images = imageJPARepository.findByBoardTypeAndBoardNum(boardType, agencynum);
            for (ImageEntity image : images) {
                s3Service.deleteFile("image/"+boardType+"/"+agencynum+"/"+image.getName());
            }
            imageJPARepository.deleteAllByBoardTypeAndBoardNum( boardType,agencynum);
            agencyJPARepository.deleteById(agencynum); //훈련소 정보날림
        }

    }





    //수정하기 아래부터  /아래코드는 수정된 이미지가 들어갈 경로를 정리
    public String makeFolder(String uploadPath, int boardType, Long boardNum) {
        String folderPath = boardType + File.separator + boardNum;
        File uploadPathFoler = new File(uploadPath, folderPath);
        if (!uploadPathFoler.exists()) {
            uploadPathFoler.mkdirs();
        }
        return folderPath;
    }

    //소개소 수정하기
    @Override
    @Transactional
    public void agencyUpdate(Long agencynum, AgencyDTO agencyDTO, MultipartFile[] files) {//사진 및 내용 업데이트
        Optional<AgencyEntity> agencyUpdate =  agencyJPARepository.findById(agencynum);
        int boardType= 7;

        if (agencyUpdate.isPresent()) {  //해당 번호의 값이 있다면 (위에서 댕겨옴)
            for (MultipartFile mf : files) {    //mf으로 해서 반복문을 돌림
                if(!mf.isEmpty()) {   //파일이 비어있지 않다면  삭제하기 위해
                    List<ImageEntity> images = imageJPARepository.findByBoardTypeAndBoardNum( boardType,agencynum);

                    for (ImageEntity image : images) {  //아마존 파일삭제
                        s3Service.deleteFile("image/" + boardType + "/" + agencyDTO.getId() + "/" + image.getName());
                    }
                    imageJPARepository.deleteAllByBoardTypeAndBoardNum( boardType, agencynum); //이미지 디비에 내용 내용삭제

                    for(MultipartFile file : files){
                        if (mf.getContentType().startsWith("image")) {
                            try {
                            String filePath = s3Service.uploadFile(file, "image/" + boardType + "/" + agencyDTO.getId());
                            ImageDTO imageDTO = new ImageDTO();
                            imageDTO.setBoardNum(agencyDTO.getId());
                            imageDTO.setBoardType(boardType);
                            imageDTO.setName(filePath);
                            imageJPARepository.save(imageDTO.toImageEntity());  //데이터베이스에 정보 저장
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        AgencyEntity agency = agencyUpdate.get();
        agency.setReadCount(agency.getReadCount());
        agencyJPARepository.save(agencyDTO.toAgencyEntity());  // 이건 내용새로 업데이트
    }


}
