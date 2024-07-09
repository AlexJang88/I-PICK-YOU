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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class RecruitServiceImpl implements RecruitService {
    @Value("${img.upload.path}")
    private String imgUploadPath;
    @Value("${contract.upload.path}")
    private String contactUploadPath;

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
    public void post(Model model, Long num, String sid) {
        Optional<RecruitEntity> post = recruitJPA.findById(num);
        RecruitDTO edto = new RecruitDTO();
        CompanyInfoDTO cidto = new CompanyInfoDTO();
        MemberDTO mdto = new MemberDTO();
        List<ImageEntity> imageList = Collections.emptyList();
        String gender = "성별 무관";
        String type = "일반";
        int favoritecheck = 0;

        if (post.isPresent()) {
            imageList = imageJPA.findByBoardTypeAndBoardNum(2, num);
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
            model.addAttribute("type", type);
            model.addAttribute("gender", gender);
            model.addAttribute("favoritecheck", favoritecheck);
            model.addAttribute("member", mdto);
            model.addAttribute("company", cidto);
            model.addAttribute("post", post.get());
            model.addAttribute("imgList", imageList);
        }

    }


    @Override
    @Transactional
    public void writePost(List<MultipartFile> files, RecruitDTO rdto, RecruitDetailDTO rddto, int BoardType) {

        Long recruitNum = recruitJPA.getAutoIncrementValue("pickyou", "recruit");
        rddto.setRecruitId(recruitNum);

        recruitJPA.save(rdto.toRecruitEntity());
        recruitDetailJPA.save(rddto.toRecruitDetailEntity());

        if (!CollectionUtils.isEmpty(files)) {
            filesUpload(files, BoardType, recruitNum, imgUploadPath);
        }

    }

    @Override
    @Transactional
    public void deletePost(Long boardNum,int boardType) {
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
                    File folder = new File(imgUploadPath + File.separator + 2 + File.separator + rdto.getId());
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
                    imageJPA.deleteAllByBoardTypeAndBoardNum(boardType, rdto.getId());
                   filesUpload(files,boardType,rdto.getId(),imgUploadPath);
                }
            }
        }
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
    public void contractPDF(HttpServletResponse response,Long id) {
        Optional<ContractEntity> con =contractJPA.findById(id);
        // 미리 준비한 DTO 선언
        ItextPdfDto itextPdfDto = new ItextPdfDto();
        // pdf 파일이 저장될 경로
        itextPdfDto.setPdfFilePath(contactUploadPath+File.separator+id+File.separator);

        // pdf 파일이 저장될 경로 ( Windows 기준 )
        // itextPdfDto.setPdfFilePath("C:\\Users\\hyeok\\Desktop\\pdf");

        // pdf 파일명 ( 계약날짜로 생성 )
        itextPdfDto.setPdfFileName(con.get().getContractDate() + ".pdf");

        // getHtml 에서 호출될 코드명
        itextPdfDto.setContractId(id);

        // ======================= PDF 존재 유무 체크 =======================
        // 없다면 PDF 파일 만들기
        File file = itextPdfUtil.checkPDF(itextPdfDto);
        int fileSize = (int) file.length();
        // ===============================================================


        // ===============================================================
        // 파일 다운로드를 위한 header 설정
        /*response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+itextPdfDto.getPdfFileName()+";");
        response.setContentLengthLong(fileSize);
        response.setStatus(HttpServletResponse.SC_OK);*/
        // ===============================================================

        // 파일 다운로드
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        // PDF 파일을 버퍼에 담은 후 다운로드
        try{
            in = new BufferedInputStream(new FileInputStream(file));
            out = new BufferedOutputStream(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                Objects.requireNonNull(out).flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void userInfo(Model model, String memberId, String companyId) {

        Optional<MemberEntity> mem = memberJPA.findById(memberId);
        Optional<MemberEntity> com = memberJPA.findById(companyId);
        if(mem.isPresent()){
            model.addAttribute("mem",mem.get());
        }
        if(com.isPresent()){
            model.addAttribute("com",com.get());
        }



    }

    @Override
    public Long contract(ContractDTO dto) {
        if(dto.getId()==0){
            dto.setId(null);
        }
        Long maxnum=contractJPA.getAutoIncrementValue("pickyou","contract");
        System.out.println("========================maxnum"+maxnum);
        ConfirmDTO cdto = new ConfirmDTO();
        cdto.setMemberId(dto.getMemberId());
        cdto.setCompanyId(dto.getCompanyId());
        cdto.setApply(1);
        cdto.setStart(Date.valueOf(dto.getStartDate()));
        cdto.setEnd(Date.valueOf(dto.getEndDate()));
        cdto.setContractId(maxnum);
        System.out.println("========================cdto"+dto);
        contractJPA.save(dto.toContractEntity());
        System.out.println("========================confirmsave"+maxnum);
        confirmJPA.save(cdto.toConfirmEntity());
        System.out.println("========================contractsave"+maxnum);

        return maxnum;
    }

    @Override
    public void getContract(HttpServletResponse response, Model model, Long id,String userId) {
        int type=10;
        String comSign="없음";
        String memSign="없음";
        File companySign =null;
        File memberSign = null;
        File contractPdf =null;
        Optional<ContractEntity>  con = contractJPA.findById(id);
        if(con.isPresent()){
            LocalDate conDate= con.get().getContractDate();
            String s = conDate.toString();
            s=s.substring(0,10);
            companySign= new File(contactUploadPath+File.separator+id+File.separator+con.get().getCompanyId()+"_signature.png");
            memberSign= new File(contactUploadPath+File.separator+id+File.separator+con.get().getMemberId()+"_signature.png");
            contractPdf =new File(contactUploadPath+File.separator+id+File.separator+s+".pdf");
            ContractEntity ce = con.get();

            System.out.println("========================ce"+ce);
            model.addAttribute("contract",ce);
            Optional<MemberEntity> member = memberJPA.findById(ce.getMemberId());
            Optional<MemberEntity> company = memberJPA.findById(ce.getCompanyId());
            if(member.isPresent()){
                model.addAttribute("member",member.get());
            }if(company.isPresent()){
                model.addAttribute("company",company.get());
            }
        }
        System.out.println("==================userId" + con.get().getCompanyId());
        System.out.println("==================userId" + con.get().getMemberId());
        System.out.println("==================userId" + userId);



        if(con.get().getCompanyId().equals(userId) || con.get().getMemberId().equals(userId)) {
            Optional<MemberEntity> mem = memberJPA.findById(userId);
            System.out.println("==================authCheck" + mem.get().getAuth());
            if (mem.get().getAuth().contains("COMPANY")) {
                System.out.println("==================authCheck" + mem.get().getAuth());
               if (companySign.exists() && !memberSign.exists()) {
                    System.out.println("==================authCheck" + mem.get().getAuth());
                    type = 11;
                    comSign = "/upload/contract/" + id + File.separator + con.get().getCompanyId() + "_signature.png";
                }if (!companySign.exists() && memberSign.exists()) {
                    System.out.println("==================authCheck" + mem.get().getAuth());
                    type = 12;
                    memSign = "/upload/contract/" + id + File.separator + con.get().getMemberId() + "_signature.png";
                }
                if (companySign.exists() && memberSign.exists()) {
                    comSign = "/upload/contract/" + id + File.separator + con.get().getCompanyId() + "_signature.png";
                    memSign = "/upload/contract/" + id + File.separator + con.get().getMemberId() + "_signature.png";
                    type = 100;
                    if(!contractPdf.exists()){
                        contractPDF(response,id);
                    }
                }
            }
            if (mem.get().getAuth().contains("USER")) {
                if (memberSign.exists() && !companySign.exists()) {
                    type = 21;
                    memSign = "/upload/contract/" + id + File.separator + con.get().getMemberId() + "_signature.png";
                    System.out.println("==================authCheck" + memSign);
                }
                if (!memberSign.exists() && companySign.exists()) {
                    type = 22;
                    comSign = "/upload/contract/" + id + File.separator + con.get().getCompanyId() + "_signature.png";
                    System.out.println("==================authCheck" + comSign);
                }
                if (companySign.exists() && memberSign.exists()) {
                    comSign = "/upload/contract/" + id + File.separator + con.get().getCompanyId() + "_signature.png";
                    memSign = "/upload/contract/" + id + File.separator + con.get().getMemberId() + "_signature.png";
                    type = 100;
                    System.out.println("==================authCheck" + comSign);
                    System.out.println("==================authCheck" + memSign);
                }
            }
        }


        model.addAttribute("signCheck",type);
        model.addAttribute("comSign",comSign);
        model.addAttribute("memSign",memSign);
    }
    @Override
    public Map<String, String> saveSignature(MultipartFile signature, Long contractId, String sid) {
        Map<String,String> sign = new HashMap<>();
        String saveName="";
        //시큐리티 세션으로 처리해야함
        if(!signature.isEmpty()){
            String folderPath = String.valueOf(contractId);
            File uploadPathFolder = new File(contactUploadPath,folderPath);
            if(!uploadPathFolder.exists()){
                uploadPathFolder.mkdirs();
            }
            String filePath = contactUploadPath+File.separator+contractId+File.separator+sid+"_"+signature.getOriginalFilename();
            File file = new File(filePath);
            try{
                signature.transferTo(file);
            }catch (IOException e){
                e.printStackTrace();
            }
            String signName = "/upload/contract/"+contractId+File.separator+sid+"_"+signature.getOriginalFilename();
            sign.put("signName",signName);
        }

        return sign;
    }


    public String makeFolder(String uploadPath, int boardType, Long boardNum) {
        String folderPath = boardType + File.separator + boardNum;
        File uploadPathFoler = new File(uploadPath, folderPath);

        if (!uploadPathFoler.exists()) {
            uploadPathFoler.mkdirs();
        }
        return folderPath;
    }
    public void filesUpload(List<MultipartFile> files, int boardType, Long BoardNum, String uploadPath) {
        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile mf : files) {
                if (mf.getContentType().startsWith("image")) {
                    String originalName = mf.getOriginalFilename();
                    String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);
                    String folderPath = makeFolder(imgUploadPath, boardType, BoardNum);
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

}

