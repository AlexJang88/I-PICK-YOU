package com.project.pickyou.repository;

import com.project.pickyou.entity.LicenceEntity;
import com.project.pickyou.entity.LicenceID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenceJPARepository extends JpaRepository<LicenceEntity, LicenceID> {
    // 이력서 면허증 가져오기
    public List<LicenceEntity> findByResumeId(Long num);

    // 이력서 면허증 삭제하기
    public void deleteAllByResumeId(Long num);
}
