package com.project.pickyou.service;

import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.TrainningDTO;
import com.project.pickyou.entity.ImageEntity;
import com.project.pickyou.entity.TrainningEntity;

import com.project.pickyou.repository.ImageJPARepository;
import com.project.pickyou.repository.TrainningJPARepository;
import jakarta.transaction.Transactional;
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

public class TrainningServiceImpl implements TrainningService{

    @Value("${img.upload.path}")
    private String imgUploadPath;  // 프로퍼티스에서 정한경로를, 웹 컨피그에서 해당 경로 지정 해서 클래스 안에 설정


    private final TrainningJPARepository trainningJPARepository;
    private final ImageJPARepository imageJPARepository;

    public TrainningServiceImpl (TrainningJPARepository trainningJPARepository, ImageJPARepository imageJPARepository){
        this.trainningJPARepository = trainningJPARepository;
        this.imageJPARepository = imageJPARepository;
    }





    // 훈련소 리스트 가져오기
    @Override
    public void trainningCompany(Model model, int pageNum) {

        int pageSize = 9;
        Long longCount = trainningJPARepository.count();
        int count = longCount.intValue();

        Sort sort = Sort.by(Sort.Order.desc("reg"));  //내림차순
        //아래 페이지 출력시 0페이지부터 진행
        Page<TrainningEntity> page = trainningJPARepository.findAll(PageRequest.of(pageNum - 1, pageSize, sort));

        List<TrainningEntity> trainninglist = page.getContent();

        model.addAttribute("trainninglist", trainninglist);
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

    }
    // 훈련소 리스트 가져오기





    //훈련내용 상세보기
    @Override
    public void Details(Model model, Long trainnignum) {  //훈련소내용 상세보기

        Optional<TrainningEntity> DetailsList = trainningJPARepository.findById(trainnignum);// 훈련내용가져오기
        List<ImageEntity> images = imageJPARepository.findByBoardTypeAndBoardNum( 3,trainnignum); // 훈련 이미지 가져오기

        model.addAttribute("images", images);
        model.addAttribute("DetailsList",DetailsList);
    }
    //훈련내용 상세보기


    //훈련정보 저장하기
    @Override
    public TrainningEntity savetrainning(TrainningDTO trainningDTO) {  //훈련정보넣기
        TrainningEntity trainningEntity = trainningJPARepository.save(trainningDTO.toTrainningEntity());

        return trainningEntity;
    }
    //훈련정보 저장하기



    /*아래는 이미지 넣기*/
    @Override
    public void saveImage(ImageDTO imageDTO, MultipartFile[] files) {

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename();  //원래사진이름
                String fileName = generateUniqueFileName(originalFileName); // 아래에서 고유한 파일 이름 생성

                try {
                    // 파일 저장할 기본 경로
                    String baseDir = imgUploadPath;
                    // 훈련소 ID 설정 과 타입 설정예정
                    int numtype = 3;  //훈련소는 3임
                    String num = "3";
                    // 훈련소 ID를 기반으로 서브 폴더 생성
                    String subFolder = num + File.separator+ imageDTO.getBoardNum();  //파일 이름 설정하는것_번호 //num = 타입 3번이고 File.separator는 안에 파일+ 파일번호
                    Path directory = Paths.get(baseDir, subFolder);   //프로퍼티스 경로와 + 파일생성된 경로 합치기

                    // 서브 폴더가 존재하지 않으면 생성
                    if (!Files.exists(directory)) {  //해당 폴더를 확인하고 없으면 생성 확인
                        Files.createDirectories(directory);
                    }

                    // 파일 경로 설정
                    Path filePath = directory.resolve(fileName);    //경로속 uu아이디 이름 넣기
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


                    /*아래5개는 사진 파일 db에 저장하는것*/
                    // 파일 정보를 DB에 저장하거나 필요한 작업 수행
                    ImageEntity imageEntity = new ImageEntity();  //new객체생성
                    imageEntity.setName(fileName);                   //이름설정
                    imageEntity.setBoardNum(imageDTO.getBoardNum()); // 이미지 DTO에서 boardNum을 가져와 설정
                    imageEntity.setBoardType(numtype);           //db타입설정
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
    /*위에는 이미지 넣기*/






    //훈련소 내용 지우기
    @Override
    @Transactional
    public void deleteDetails(Long trainnignum) {

        trainningJPARepository.deleteById(trainnignum); //훈련소 정보 날리는 방법

    }

    /*훈련소 내용속 이미지 지우기*/
    @Override
    @Transactional
    public void deleteDetailsImg(Long trainnignum) {

        Optional<TrainningEntity> trainning = trainningJPARepository.findById(trainnignum);
        if (trainning.isPresent()) {
            File folder = new File(imgUploadPath + File.separator + 3 + File.separator + trainnignum);
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
        }

        imageJPARepository.deleteAllByBoardTypeAndBoardNum( 3,trainnignum);

    }
    /*훈련소 내용속 이미지 지우기*/






    //수정하기 아래부터  /아래코드는 수정된 이미지가 들어갈 경로를 정리
    public String makeFolder(String uploadPath, int boardType, Long boardNum) {
        String folderPath = boardType + File.separator + boardNum;
        File uploadPathFoler = new File(uploadPath, folderPath);

        if (!uploadPathFoler.exists()) {
            uploadPathFoler.mkdirs();
        }
        return folderPath;
    }

    /*수정하기여기서부터*/
    @Override
    @Transactional
    public void trainningUpdate(Long trainnignum, TrainningDTO trainningDTO, MultipartFile[] files) {  //사진 및 내용 업데이트
        Optional<TrainningEntity> trainningUpdate =  trainningJPARepository.findById(trainnignum);
            int check = 0;

            //기존이미지가 남아있다면
        if (trainningUpdate.isPresent()) {  //해당 번호의 값이 있다면 (위에서 댕겨옴)
            for (MultipartFile mf : files) {    //mf으로 해서 반복문을 돌림
                if(!mf.isEmpty()) {   //파일이 비어있지 않다면  삭제하기 위해
                    if(check==0) {
                        File folder = new File(imgUploadPath + File.separator + 3 + File.separator + trainnignum);  //해당 경로의 파일을삭제하기위해
                        try {
                            if (folder.exists()) {
                                FileUtils.cleanDirectory(folder);    //실제폴더내용삭제
                            }
                            if (folder.isDirectory()) {
                                folder.delete();                    //실제폴더삭제
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        imageJPARepository.deleteAllByBoardTypeAndBoardNum( 3,trainnignum); //데이터베이스 속 이미지 테이블 내용삭제
                        check++;
                    }


                    //새로운 이미지 설정하기
                    if (mf.getContentType().startsWith("image")) {
                        String originalName = mf.getOriginalFilename();

                        String folderPath = makeFolder(imgUploadPath, 3, trainnignum);  //폴더경로


                        String uuid = UUID.randomUUID().toString();                              //이름값 설정
                        String ext = originalName.substring(originalName.lastIndexOf("."));  //확장자 가져오기
                        String saveName = folderPath + File.separator + uuid + ext;            //파일 이름 만들기


                        ImageDTO imageDTO = new ImageDTO();
                        imageDTO.setBoardNum(trainnignum);
                        imageDTO.setBoardType(3);
                        imageDTO.setName(uuid + ext);
                        imageJPARepository.save(imageDTO.toImageEntity());  //데이터베이스에 정보 저장
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

        TrainningEntity trainning = trainningUpdate.get();
        trainning.setReadCount(trainningDTO.getReadCount());
        trainningJPARepository.save(trainningDTO.toTrainningEntity());  // 이건 내용새로 업데이트
    }




    @Override
    public void trainngCount(Long trainnignum) {  //훈련소상세보기 조회수

        Optional<TrainningEntity> optional = trainningJPARepository.findById(trainnignum); //값 가져오기

        if(optional.isPresent()){
            TrainningEntity traing = optional.get();

            traing.setReadCount(traing.getReadCount() + 1);


            trainningJPARepository.save(traing);
        }


    }
    /*수정하기여기까지*/





}
