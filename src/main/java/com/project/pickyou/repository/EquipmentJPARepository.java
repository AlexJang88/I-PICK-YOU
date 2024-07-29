package com.project.pickyou.repository;

import com.project.pickyou.entity.EquipmentEntity;
import com.project.pickyou.entity.EquipmentID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentJPARepository extends JpaRepository<EquipmentEntity, EquipmentID> {
    // 이력서 보유장비 가져오기
    public List<EquipmentEntity> findByResumeId(Long num);

    // 이력서 보유장비 삭제하기
    public void deleteAllByResumeId(Long num);
}
