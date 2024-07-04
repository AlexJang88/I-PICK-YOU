package com.project.pickyou.repository;

import com.project.pickyou.entity.RecruitStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitStateJPARepository extends JpaRepository<RecruitStateEntity,Long> {
    public Optional<RecruitStateEntity> findByRecruitIdAndMemberId(Long boardNum,String id);
}
