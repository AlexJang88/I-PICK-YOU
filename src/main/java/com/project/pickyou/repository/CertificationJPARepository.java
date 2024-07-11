package com.project.pickyou.repository;

import com.project.pickyou.entity.CertificationEntity;
import com.project.pickyou.entity.CertificationID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationJPARepository extends JpaRepository<CertificationEntity, CertificationID> {
    // 이력서 이수증 가져오기
    public List<CertificationEntity> findByResumeId(Long num);

    // 이력서 이수증 삭제하기
    public void deleteAllByResumeId(Long num);
}
