package com.project.pickyou.repository;

import com.project.pickyou.entity.CareerEntity;
import com.project.pickyou.entity.CareerID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CareerJPARepository extends JpaRepository<CareerEntity, CareerID> {
    // 이력서 경력 가져오기
    public Optional<CareerEntity> findByResumeId(Long num);

    // 이력서 경력 삭제하기
    public void deleteByResumeId(Long num);
}
