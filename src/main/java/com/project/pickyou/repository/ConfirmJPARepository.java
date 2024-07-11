package com.project.pickyou.repository;

import com.project.pickyou.entity.ConfirmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConfirmJPARepository extends JpaRepository<ConfirmEntity,Long> {
    public List<ConfirmEntity> findByRecruitId(Long recruitId);
    public Long countByRecruitId(Long recruitId);
    public Page<ConfirmEntity> findByRecruitId(Long recruitId, Pageable pageable);
    @Transactional
    public void deleteByMemberIdAndRecruitId(String memberId,Long recruitId);
}
