package com.project.pickyou.service;

import com.project.pickyou.dto.*;
import com.project.pickyou.entity.*;
import com.project.pickyou.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeJPARepository resumeJPA;
    private final JobJPARepository jobJPA;
    private final CareerJPARepository careerJPA;
    private final LicenceJPARepository licenceJPA;
    private final EquipmentJPARepository equipmentJPA;
    private final CertificationJPARepository certificationJPA;
    private final ConfirmJPARepository comfirmJPA;

    // 이력서 리스트 가져오기
    @Override
    public void selectResume(Model model, String memberId) {
        List<ResumeEntity> resumeList = resumeJPA.findByMemberIdOrderByRegDesc(memberId);
        List<ResumeDTO> resumeDTOList = new ArrayList<>();

        model.addAttribute("resumeList", resumeList);
    }

    // 이력서 인서트
    @Override
    public void resumeInsert(ResumeDTO Rdto, String careerName, List<String> jobNames,
                             List<String> licenceNames, List<String> equipmentNames, List<String> certificationNames) {

        // 이력서 현재 시퀀스값 +1 가져오기
        Long num = resumeJPA.getAutoIncrementValue("pickyou", "resume");

        // 이력서 인서트
        Rdto.setRegType(2);
        resumeJPA.save(Rdto.toResumeEntity());

        // 이력서 직종 인서트
        for (String jobName : jobNames) {
            JobDTO Jdto = new JobDTO();
            Jdto.setResumeId(num);
            Jdto.setName(jobName);
            jobJPA.save(Jdto.toJobEntity());
        }

        // 이력서 경력 인서트
        CareerDTO Cdto = new CareerDTO();
        Cdto.setResumeId(num);
        Cdto.setName(careerName);
        careerJPA.save(Cdto.toCareerEntity());

        // 이력서 면허증 인서트
        for (String licenceName : licenceNames) {
            LicenceDTO Ldto = new LicenceDTO();
            Ldto.setResumeId(num);
            Ldto.setName(licenceName);
            licenceJPA.save(Ldto.toLicenceEntity());
        }

        // 이력서 보유장비 인서트
        for (String equipmentName : equipmentNames) {
            EquipmentDTO Edto = new EquipmentDTO();
            Edto.setResumeId(num);
            Edto.setName(equipmentName);
            equipmentJPA.save(Edto.toEquipmentEntity());
        }

        // 이력서 이수증 인서트
        for (String certificationName : certificationNames) {
            CertificationDTO dto = new CertificationDTO();
            dto.setResumeId(num);
            dto.setName(certificationName);
            certificationJPA.save(dto.toCertificationEntity());
        }
    }

    // 이력서 상세정보
    @Override
    public void selectResumeInfo(Model model, Long num) {
        // 이력서 상세정보 가져오기
        Optional<ResumeEntity> resumeEntity = resumeJPA.findById(num);
        if (resumeEntity.isPresent()) {
            ResumeEntity resumeInfo = resumeEntity.get();
            model.addAttribute("resumeInfo", resumeInfo);


            // 생년월일에 맞게 나이로 바꾸는 코드
            String birthdayString = resumeInfo.getMember().getMemberInfo().getBirth();
            LocalDate birthday = LocalDate.parse(birthdayString);
            int currentYear = LocalDate.now().getYear();
            int birthYear = birthday.getYear();
            int age = currentYear - birthYear;

            // 성별
            int genderNum = resumeInfo.getMember().getMemberInfo().getGender();
            String gender = getGender(genderNum);

            // 성별 + 나이
            String ageStr = String.valueOf(age);
            String genderAge = gender + "    " + ageStr + "  세";
            model.addAttribute("genderAge", genderAge);

            // 주소
            String getAddress = resumeInfo.getMember().getAddress();
            String[] addressParts = getAddress.split(" "); // 공백을 기준으로 분리
            String address = addressParts[0] + "    " + addressParts[1];
            model.addAttribute("address", address);
        }

        // 이력서 경력 가져오기
        Optional<CareerEntity> careerEntity = careerJPA.findByResumeId(num);
        if (careerEntity.isPresent()) {
            CareerEntity careerInfo = careerEntity.get();
            model.addAttribute("careerInfo", careerInfo);
        }

        // 이력서 이수증 가져오기
        List<CertificationEntity> certificationEntity = certificationJPA.findByResumeId(num);
        model.addAttribute("certificationInfo", certificationEntity);

        // 이력서 보유장비 가져오기
        List<EquipmentEntity> equipmentEntity = equipmentJPA.findByResumeId(num);
        model.addAttribute("equipmentInfo", equipmentEntity);

        // 이력서 직종 가져오기
        List<JobEntity> jobEntity = jobJPA.findByResumeId(num);
        model.addAttribute("jobInfo", jobEntity);

        // 이력서 면허증 가져오기
        List<LicenceEntity> licenceEntity = licenceJPA.findByResumeId(num);
        model.addAttribute("licenceInfo", licenceEntity);
    }

    // 이력서 삭제
    @Override
    @Transactional
    public void deleteResume(Long num) {
        certificationJPA.deleteAllByResumeId(num);
        equipmentJPA.deleteAllByResumeId(num);
        licenceJPA.deleteAllByResumeId(num);
        jobJPA.deleteAllByResumeId(num);
        careerJPA.deleteByResumeId(num);
        resumeJPA.deleteById(num);
    }

    // 이력서 업데이트
    @Transactional
    @Override
    public void resumeUpdate(ResumeDTO Rdto, String careerName, List<String> jobNames, Long num,
                             List<String> licenceNames, List<String> equipmentNames, List<String> certificationNames) {

        if (Rdto.getRegType() == 1) {
            Optional<ResumeEntity> resumeOptional = resumeJPA.findByRegTypeAndMemberId(1, Rdto.getMemberId());
            if (resumeOptional.isPresent()) {
                ResumeEntity resumeInfo = resumeOptional.get();
                resumeInfo.setRegType(2);
                resumeJPA.save(resumeInfo);
            }
        }

        // 이력서 업데이트
        LocalDate reg = LocalDate.now();
        Timestamp timestamp = Timestamp.valueOf(reg.atStartOfDay());
        Rdto.setReg(timestamp);
        Rdto.setId(num);
        resumeJPA.save(Rdto.toResumeEntity());

        // 이력서 직종 업데이트
        List<JobEntity> jobEntityList = jobJPA.findByResumeId(num);
        jobJPA.deleteAll(jobEntityList);

        for (String jobName : jobNames) {
            JobDTO Jdto = new JobDTO();
            Jdto.setResumeId(num);
            Jdto.setName(jobName);
            jobJPA.save(Jdto.toJobEntity());
        }

        // 이력서 경력 업데이트
        Optional<CareerEntity> careerOptional = careerJPA.findByResumeId(num);
        if (careerOptional.isPresent()) {
            careerJPA.deleteByResumeId(num);
            CareerDTO Cdto = new CareerDTO();
            Cdto.setResumeId(num);
            Cdto.setName(careerName);
            careerJPA.save(Cdto.toCareerEntity());
        }

        // 이력서 면허증 업데이트
        List<LicenceEntity> licenceEntityList = licenceJPA.findByResumeId(num);
        licenceJPA.deleteAll(licenceEntityList);

        for (String licenceName : licenceNames) {
            LicenceDTO Ldto = new LicenceDTO();
            Ldto.setResumeId(num);
            Ldto.setName(licenceName);
            licenceJPA.save(Ldto.toLicenceEntity());
        }

        // 이력서 보유장비 업데이트
        List<EquipmentEntity> equipmentEntityList = equipmentJPA.findByResumeId(num);
        equipmentJPA.deleteAll(equipmentEntityList);

        for (String equipmentName : equipmentNames) {
            EquipmentDTO Edto = new EquipmentDTO();
            Edto.setResumeId(num);
            Edto.setName(equipmentName);
            equipmentJPA.save(Edto.toEquipmentEntity());
        }

        // 이력서 이수증 업데이트
        List<CertificationEntity> certificationEntityList = certificationJPA.findByResumeId(num);
        certificationJPA.deleteAll(certificationEntityList);

        for (String certificationName : certificationNames) {
            CertificationDTO dto = new CertificationDTO();
            dto.setResumeId(num);
            dto.setName(certificationName);
            certificationJPA.save(dto.toCertificationEntity());
        }
    }

    // 이력서 공개인 리스트
    @Override
    public void AllPosts(Model model, int pageNum) {
        int pageSize = 9;

        int count = resumeJPA.countByRegType(1);

        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<ResumeEntity> page = resumeJPA.findAllByRegType(1, PageRequest.of(pageNum - 1, pageSize, sort));

        List<ResumeEntity> posts = page.getContent();

        List<String> jobName = new ArrayList<>();
        List<String> careerName = new ArrayList<>();
        List<String> genderAges = new ArrayList<>();

        for (ResumeEntity post : posts) {

            // Job names 리스트에 추가
            for (JobEntity job : post.getJob()) {
                jobName.add(job.getName());
            }


            // Career name 추가
            if (post.getCareer() != null) {
                careerName.add(post.getCareer().getName());
            }

            // 생년월일에 맞게 나이로 바꾸는 코드
            String birthdayString = post.getMember().getMemberInfo().getBirth();
            LocalDate birthday = LocalDate.parse(birthdayString);
            int currentYear = LocalDate.now().getYear();
            int birthYear = birthday.getYear();
            int age = currentYear - birthYear;

            // 성별
            int genderNum = post.getMember().getMemberInfo().getGender();
            String gender = getGender(genderNum);

            // 성별 + 나이
            String ageStr = String.valueOf(age);
            String genderAge = gender + "    " + ageStr + "  세";
            genderAges.add(genderAge); // genderAges 리스트에 추가
        }

        model.addAttribute("genderAges", genderAges);

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

    // 사업자 입장에서 이력서보고 채용 (인서트)
    @Override
    public void confirmInsert(ConfirmDTO dto) {
        comfirmJPA.save(dto.toConfirmEntity());
    }

    // 지원자 목록에서 memberId로 이력서 번호찾기
    @Override
    public Optional<ResumeEntity> findBymemberId(String memberId, int regType) {
        return resumeJPA.findByRegTypeAndMemberId(regType, memberId);
    }

    // 성별
    private String getGender(int genderNum) {
        if (genderNum == 1 || genderNum == 3) {
            return "남";
        } else {
            return "여";
        }
    }

    // 이력서 공개여부
    private String getRegType(int regNum) {
        if (regNum == 1) {
            return "공개";
        } else {
            return "비공개";
        }
    }
}