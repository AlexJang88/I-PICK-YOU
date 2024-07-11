package com.project.pickyou.repository;

import com.project.pickyou.entity.JobEntity;
import com.project.pickyou.entity.JobID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobJPARepository extends JpaRepository<JobEntity, JobID> {
    // 이력서 직종 가져오기
    public List<JobEntity> findByResumeId(Long num);

    // 이력서 직종 삭제하기
    public void deleteAllByResumeId(Long num);
}
