package com.project.pickyou.repository;

import com.project.pickyou.entity.ConfirmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ConfirmJPARepository extends JpaRepository<ConfirmEntity,Long> {
    public List<ConfirmEntity> findByRecruitIdAndApplyNot(Long recruitId,int type);
    public Long countByRecruitIdAndApplyNot(Long recruitId,Integer type);
    public Page<ConfirmEntity> findByRecruitIdAndApplyNot(Long recruitId,Integer type, Pageable pageable);
    public Long countByMemberIdAndCompanyId(String member,String company);
    @Transactional
    public void deleteByMemberIdAndRecruitId(String memberId,Long recruitId);
    public Optional<ConfirmEntity> findByMemberIdAndRecruitIdAndApplyNot(String memberId,Long recruitId,int type);
}
