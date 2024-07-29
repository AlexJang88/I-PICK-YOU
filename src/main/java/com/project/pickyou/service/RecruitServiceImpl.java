package com.project.pickyou.service;

import com.project.pickyou.dto.*;
import com.project.pickyou.entity.*;
import com.project.pickyou.handler.ItextPdfUtil;
import com.project.pickyou.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class RecruitServiceImpl implements RecruitService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    private final S3Service s3Service;
    private final MemberJPARepository memberJPA;
    private final ItextPdfUtil itextPdfUtil;
    private final RecruitJPARepository recruitJPA;
    private final RecruitStateJPARepository recruitStateJPA;
    private final ImageJPARepository imageJPA;
    private final PickJPARepository pickJPA;
    private final ResumeJPARepository resumeJPA;
    private final RecruitDetailJPARepository recruitDetailJPA;
    private final ContractJPARepository contractJPA;
    private final ConfirmJPARepository confirmJPA;
    private final SatisfactionServiceImpl satisfactionService;

    @Override
    public void AllPosts(Model model, int pageNum,int checkType) {
        int pageSize = 10;
        Long longCount=0L;
        int count=0;

        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<RecruitEntity> page =null;
        List<RecruitEntity> posts =null;

        if(checkType==1) {
            longCount = recruitJPA.count();
            count = longCount.intValue();
             page = recruitJPA.findAll(PageRequest.of(pageNum - 1, pageSize, sort));
             posts = page.getContent();
        } else if (checkType==2) {
            longCount = recruitJPA.countByStatus(1);
            count = longCount.intValue();
            page = recruitJPA.findByStatus(1,PageRequest.of(pageNum - 1, pageSize, sort));
            posts = page.getContent();
        } else if (checkType==3) {
            longCount = recruitJPA.countByStatus(2);
            count = longCount.intValue();
            page = recruitJPA.findByStatus(2,PageRequest.of(pageNum - 1, pageSize, sort));
            posts = page.getContent();
        }
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
    public void myPosts(Model model, int pageNum, String id) {
        int pageSize = 10;
        Long longCount = recruitJPA.countByMemberId(id);
        int count = longCount.intValue();

        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<RecruitEntity> page = recruitJPA.findByMemberId(id,PageRequest.of(pageNum - 1, pageSize, sort));

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
        model.addAttribute("bucketName",bucket);
        model.addAttribute("regionName",region);
        satisfactionService.myScore(model,id,2);

    }

    @Override
    public void post(Model model, Long num, String sid,int boardType) {
        int check=0;
        int auth=0;
        int applyCheck=0;
        Optional<RecruitEntity> post = recruitJPA.findById(num);
        Optional<MemberEntity> member=memberJPA.findById(sid);
        RecruitDTO edto = new RecruitDTO();
        CompanyInfoDTO cidto = new CompanyInfoDTO();
        MemberDTO mdto = new MemberDTO();
        List<ImageEntity> imageList = Collections.emptyList();
        String gender = "성별 무관";
        String type = "일반";
        int favoritecheck = 0;

        if (post.isPresent()) {
            if(post.get().getMemberId().equals(sid)){
                check=1;
            }
            if(member.isPresent()) {
                if (member.get().getAuth().contains("USER")) {
                    auth = 1;
                } else if (member.get().getAuth().contains("COMPANY")) {
                    auth = 2;
                } else if (member.get().getAuth().contains("ADMIN")) {
                    auth = 99;
                }
            }
            if(resumeJPA.findByMemberIdAndRegType(sid,1).isPresent()){
                applyCheck=1;
                if(recruitStateJPA.countByMemberIdAndRecruitId(sid,num)>0){
                    applyCheck=2;
                }
            }

            imageList = imageJPA.findByBoardTypeAndBoardNum(boardType, num);
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
            model.addAttribute("applyCheck",applyCheck);
            model.addAttribute("auth",auth);
            model.addAttribute("check",check);
            model.addAttribute("type", type);
            model.addAttribute("gender", gender);
            model.addAttribute("favoritecheck", favoritecheck);
            model.addAttribute("member", mdto);
            model.addAttribute("company", cidto);
            model.addAttribute("post", post.get());
            model.addAttribute("imgList", imageList);
            model.addAttribute("bucketName",bucket);
            model.addAttribute("regionName",region);
        }

    }


    @Override
    @Transactional
    public void writePost(List<MultipartFile> files, RecruitDTO rdto, RecruitDetailDTO rddto, int BoardType) {

        Long recruitNum = recruitJPA.getAutoIncrementValue("pickyou", "recruit");
        rddto.setRecruitId(recruitNum);



        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                if (file.getContentType().startsWith("image")) {
                    try {
                        String filePath = s3Service.uploadFile(file, "image/" + BoardType + "/" + recruitNum);
                        ImageDTO idto = new ImageDTO();
                        idto.setBoardNum(recruitNum);
                        idto.setBoardType(BoardType);
                        idto.setName(filePath);
                        imageJPA.save(idto.toImageEntity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        recruitJPA.save(rdto.toRecruitEntity());
        recruitDetailJPA.save(rddto.toRecruitDetailEntity());

    }

    @Override
    @Transactional
    public void deletePost(Long boardNum,int boardType) {
        Optional<RecruitEntity> recruit = recruitJPA.findById(boardNum);
        if (recruit.isPresent()) {
            List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(boardType, boardNum);
            for (ImageEntity image : images) {
                s3Service.deleteFile("image/"+boardType+"/"+boardNum+"/"+image.getName());
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
                    List<ImageEntity> images = imageJPA.findByBoardTypeAndBoardNum(boardType, rdto.getId());
                    for (ImageEntity image : images) {
                        s3Service.deleteFile("image/" + boardType + "/" + rdto.getId() + "/" + image.getName());
                    }
                    imageJPA.deleteAllByBoardTypeAndBoardNum(boardType, rdto.getId());
                    for (MultipartFile file : files) {
                        if (file.getContentType().startsWith("image")) {
                            try {
                                String filePath = s3Service.uploadFile(file, "image/" + boardType + "/" + rdto.getId());
                                ImageDTO idto = new ImageDTO();
                                idto.setBoardNum(rdto.getId());
                                idto.setBoardType(boardType);
                                idto.setName(filePath);
                                imageJPA.save(idto.toImageEntity());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        Date date = new Date();
        rdto.setReg(date);
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

    @Override
    public void contractPDF(HttpServletResponse response, Long id) {
        Optional<ContractEntity> con = contractJPA.findById(id);

        if (con.isPresent()) {
            LocalDate contractDate = con.get().getContractDate();
            String pdfFileName = contractDate + ".pdf";
            String s3FilePath =  "contract/"+id + "/" + pdfFileName;


            // Check if PDF already exists in S3
            if (!s3Service.fileExists(s3FilePath)) {
                // Generate PDF file and upload to S3
                ItextPdfDto itextPdfDto = new ItextPdfDto();
                itextPdfDto.setPdfFilePath("contract/"+id + "/");
                System.out.println("--------------------------------path"+itextPdfDto.getPdfFileName());
                itextPdfDto.setPdfFileName(pdfFileName);
                System.out.println("--------------------------------path"+itextPdfDto.getPdfFileName());
                itextPdfDto.setContractId(id);

                itextPdfUtil.checkPDF(itextPdfDto);
                // Remove local PDF file after uploading to S3
            }
            // Serve the PDF file to the client
           /* try {
                s3Service.downloadFileToResponse(s3FilePath,response, pdfFileName );
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }

    }

    @Override
    public void userInfo(Model model, String memberId, String companyId,Long recruitId,int type) {

        Optional<MemberEntity> mem = memberJPA.findById(memberId);
        Optional<MemberEntity> com = memberJPA.findById(companyId);
        if(type==1){
            model.addAttribute("applyType",type);
        } else if (type==3) {
            model.addAttribute("applyType",4);
        }
        if(mem.isPresent()){
            model.addAttribute("mem",mem.get());
        }
        if(com.isPresent()){
            model.addAttribute("com",com.get());
        }
        model.addAttribute("stateId",recruitId);



    }

    @Override
    public Long contract(ContractDTO dto,Long stateId,int applyType) {
        if(dto.getId()==0){
            dto.setId(null);
        }
        Long maxnum=contractJPA.getAutoIncrementValue("pickyou","contract");
        ConfirmDTO cdto = new ConfirmDTO();
        cdto.setMemberId(dto.getMemberId());
        cdto.setCompanyId(dto.getCompanyId());
        if(applyType==1) {
            cdto.setApply(1);
        } else if (applyType==4) {
            cdto.setApply(4);
        }
        cdto.setContractId(maxnum);
        if(stateId!=0) {
            cdto.setRecruitId(stateId);
        }
        contractJPA.save(dto.toContractEntity());
        confirmJPA.save(cdto.toConfirmEntity());
        return maxnum;
    }

    @Override
    public void getContract(HttpServletResponse response, Model model, Long id,String userId) {
        int type=10;
        int PDF=0;
        String pdfFileName="";
        String PDFUrl="";
        String comSign="없음";
        String memSign="없음";
        String companySignPath  =null;
        String memberSignPath = null;
        String contractPdfPath =null;
        Optional<ContractEntity>  con = contractJPA.findById(id);
        if(con.isPresent()){
            ContractEntity ce = con.get();
            LocalDate conDate = ce.getContractDate();
            pdfFileName = conDate.toString().substring(0, 10) + ".pdf";
            companySignPath = "contract/" + id + "/" + ce.getCompanyId() + "_signature.png";
            memberSignPath= "contract/" + id + "/" + ce.getMemberId() + "_signature.png";
            contractPdfPath ="contract/" + id + "/" + pdfFileName;
            model.addAttribute("contract",ce);
            Optional<MemberEntity> member = memberJPA.findById(ce.getMemberId());
            Optional<MemberEntity> company = memberJPA.findById(ce.getCompanyId());
            if(member.isPresent()){
                model.addAttribute("member",member.get());
            }if(company.isPresent()){
                model.addAttribute("company",company.get());
            }
        }



        if(con.get().getCompanyId().equals(userId) || con.get().getMemberId().equals(userId)) {
            Optional<MemberEntity> mem = memberJPA.findById(userId);
            if (mem.get().getAuth().contains("COMPANY")) {
               if (s3Service.fileExists(companySignPath) && !s3Service.fileExists(memberSignPath)) {
                    type = 11;
                   comSign = s3Service.getPublicUrl(companySignPath);
                }if (!s3Service.fileExists(companySignPath) && s3Service.fileExists(memberSignPath)) {
                    type = 12;
                    memSign = s3Service.getPublicUrl(memberSignPath);
                }
                if (s3Service.fileExists(companySignPath) && s3Service.fileExists(memberSignPath)) {
                    comSign = s3Service.getPublicUrl(companySignPath);
                    memSign = s3Service.getPublicUrl(memberSignPath);
                    type = 100;
                    PDF=1;
                    if(!s3Service.fileExists(contractPdfPath)){
                        contractPDF(response,id);
                    }
                }
            }
            if (mem.get().getAuth().contains("USER")) {
                if (s3Service.fileExists(memberSignPath) && !s3Service.fileExists(companySignPath)) {
                    type = 21;
                    memSign = s3Service.getPublicUrl(memberSignPath);
                }
                if (!s3Service.fileExists(memberSignPath) && s3Service.fileExists(companySignPath)) {
                    type = 22;
                    comSign = s3Service.getPublicUrl(companySignPath);
                }
                if (s3Service.fileExists(companySignPath) && s3Service.fileExists(memberSignPath)) {
                    comSign = s3Service.getPublicUrl(companySignPath);
                    memSign = s3Service.getPublicUrl(memberSignPath);
                    type = 100;
                    PDF=1;
                    if(!s3Service.fileExists(contractPdfPath)){
                        contractPDF(response,id);
                    }
                }
            }
        }

        model.addAttribute("PDF",PDF);
        model.addAttribute("signCheck",type);
        model.addAttribute("comSign",comSign);
        model.addAttribute("memSign",memSign);
        model.addAttribute("bucketName",bucket);
        model.addAttribute("regionName",region);
        model.addAttribute("pdfFileName",contractPdfPath);
    }
    @Override
    public Map<String, String> saveSignature(MultipartFile signature, Long contractId, String sid) {
        Map<String,String> sign = new HashMap<>();
        if (!signature.isEmpty()) {
            // Construct the file path for S3
            String s3FilePath = "contract/" + contractId + "/" + sid + "_signature.png";
            try {
                // Upload the signature file to S3
                String filename =s3Service.uplaodSign(signature, s3FilePath);
                // Return the S3 file path (or a public URL if your bucket is public)
                sign.put("signName", s3FilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sign;
    }

    @Override
    public void basicContract(String memberId, String companyId,int type,Long stateId) {
            ConfirmDTO cdto = new ConfirmDTO();
            cdto.setMemberId(memberId);
            cdto.setCompanyId(companyId);
            cdto.setApply(type);
            confirmJPA.save(cdto.toConfirmEntity());
    }

    @Override
    public void updateReadCount(Long boardNum) {
        Optional<RecruitEntity> oee = recruitJPA.findById(boardNum);
        if(oee.isPresent()){
            RecruitEntity re=oee.get();
            re.setReadCount(re.getReadCount()+1);
            recruitJPA.save(re);
        }
    }

    @Override
    public void mainList(Model model) {

        List<RecruitEntity> argentPost =recruitJPA.findByStatus(2);
        List<RecruitEntity> normalPost=recruitJPA.findByStatus(1);

        model.addAttribute("argentPost",argentPost);
        model.addAttribute("normalPost",normalPost);



    }


    public String makeFolder(String uploadPath, int boardType, Long boardNum) {
        String folderPath = boardType + File.separator + boardNum;
        File uploadPathFoler = new File(uploadPath, folderPath);

        if (!uploadPathFoler.exists()) {
            uploadPathFoler.mkdirs();
        }
        return folderPath;
    }

    //0 * * * * ? -매분
    //0 0 0/1 * * * -1시간마다
    @Scheduled(cron = "0 0 0/1 * * *")
    public void overTimeCheck(){
       Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,-1);
        Date lastDate = cal.getTime();
        recruitJPA.deleteByStatusAndRegLessThanEqual(2,lastDate);

    }

}

