package com.project.pickyou.service;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.TrainningDTO;
import com.project.pickyou.entity.ImageEntity;
import com.project.pickyou.entity.TrainningEntity;

import com.project.pickyou.repository.ImageJPARepository;
import com.project.pickyou.repository.TrainningJPARepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainningServiceImpl implements TrainningService{

    private final TrainningJPARepository trainningJPARepository;
    private final ImageJPARepository imageJPARepository;

    public TrainningServiceImpl (TrainningJPARepository trainningJPARepository, ImageJPARepository imageJPARepository){
        this.trainningJPARepository = trainningJPARepository;
        this.imageJPARepository = imageJPARepository;
    }


    @Override
    public void trainningCompany(Model model) {  // 훈련소 리스트 가져오기

        List<TrainningEntity> trainninglist = trainningJPARepository.findAll();

        model.addAttribute("trainninglist", trainninglist);
    }

    @Override
    public void Details(Model model, Long trainnignum) {  //훈련소내용 상세보기

        Optional<TrainningEntity> DetailsList = trainningJPARepository.findById(trainnignum);// 훈련내용가져오기
        List<ImageEntity> images = imageJPARepository.findByBoardNumAndBoardType(trainnignum, 3); // 훈련 이미지 가져오기
        
        model.addAttribute("images", images);
        model.addAttribute("DetailsList",DetailsList);
    }

    @Override
    public TrainningEntity savetrainning(TrainningDTO trainningDTO) {  //훈련정보넣기
        TrainningEntity trainningEntity = trainningJPARepository.save(trainningDTO.toTrainningEntity());

        return trainningEntity;
    }


    /*이미지 넣기*/
    @Override
    public void saveImage(ImageDTO imageDTO, MultipartFile[] files) {

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename();  //원래사진이름
                String fileName = generateUniqueFileName(originalFileName); // 아래에서 고유한 파일 이름 생성

                try {
                    // 파일 저장할 기본 경로
                    String baseDir = "C:\\Users\\trainning";

                    // 훈련소 ID를 기반으로 서브 폴더 생성
                    String subFolder = "trainning_" + imageDTO.getBoardNum();  //파일 이름 설정하는것_번호
                    Path directory = Paths.get(baseDir, subFolder);   //경로추가

                    // 서브 폴더가 존재하지 않으면 생성
                    if (!Files.exists(directory)) {  //해당 폴더를 확인하고 없으면 생성 확인
                        Files.createDirectories(directory);
                    }

                    // 파일 경로 설정
                    Path filePath = directory.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    // 파일 정보를 DB에 저장하거나 필요한 작업 수행
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setName(fileName);

                    // 훈련소 ID 설정 과 타입 설정예정
                    int numtype = 3;  //훈련소는 3임

                    imageEntity.setBoardNum(imageDTO.getBoardNum()); // 이미지 DTO에서 boardNum을 가져와 설정
                    imageEntity.setBoardType(numtype);

                    imageJPARepository.save(imageEntity); // 이미지 엔티티 저장

                } catch (IOException e) {
                    e.printStackTrace();
                    // 파일 저장 실패 시 예외 처리
                }
            }
        }
    }

    private String generateUniqueFileName(String originalFileName) {   //저장된 이미지 이름 변경
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFileName);
        return uuid + "." + extension;
    }

    private String getFileExtension(String fileName) {   //확장자 추출
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    /*이미지 넣기*/

}
