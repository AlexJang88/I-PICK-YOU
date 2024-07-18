package com.project.pickyou.service;

import com.project.pickyou.dto.ConfirmDTO;
import com.project.pickyou.dto.JobDTO;
import com.project.pickyou.dto.ResumeDTO;
import org.springframework.ui.Model;

import java.util.List;

public interface ResumeService {

    // 이력서 리스트 가져오기
    public void selectResume(Model model, String memberId);

    // 이력서 인서트
    public void resumeInsert(ResumeDTO Rdto, String careerName, List<String> jobNames,
                             List<String> licenceNames, List<String> equipmentNames, List<String> certificationNames);

    // 이력서 상세정보
    public void selectResumeInfo(Model model, Long num);

    // 이력서 삭제
    public void deleteResume(Long num);

    // 이력서 업데이트
    public void resumeUpdate(ResumeDTO Rdto, String careerName, List<String> jobNames, Long num,
                             List<String> licenceNames, List<String> equipmentNames, List<String> certificationNames);

    // 공개된 전체 이력서
    public void AllPosts(Model model, int pageNum);

    // 사업자 입장에서 이력서보고 채용 (인서트)
    public void confirmInsert(ConfirmDTO dto);
}
