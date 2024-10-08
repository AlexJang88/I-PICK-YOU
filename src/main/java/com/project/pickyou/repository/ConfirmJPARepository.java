package com.project.pickyou.repository;

import com.project.pickyou.entity.ConfirmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ConfirmJPARepository extends JpaRepository<ConfirmEntity,Long> {
    public List<ConfirmEntity> findByRecruitIdAndApplyLessThanEqual(Long recruitId,int type);
    public Long countByRecruitIdAndApplyLessThanEqual(Long recruitId,Integer type);
    public Page<ConfirmEntity> findByRecruitIdAndApplyLessThanEqual(Long recruitId,Integer type, Pageable pageable);
    public Long countByMemberIdAndCompanyId(String member,String company);
    @Transactional
    public void deleteByMemberIdAndRecruitId(String memberId,Long recruitId);
    public Optional<ConfirmEntity> findByMemberIdAndRecruitIdAndApplyLessThanEqual(String memberId,Long recruitId,int type);
    public List<ConfirmEntity> findByCompanyId(String companyId);
    // 유저 입장 채용현황
    public Page<ConfirmEntity> findByMemberId(String memberId, Pageable pageable);
    public List<ConfirmEntity> findByMemberId(String memberId);
    public int countByMemberId(String memberId);

    // 고용 요청 내역(사업자가 요청한것)
    public Page<ConfirmEntity> findByCompanyIdAndApply(String companyId, int apply, Pageable pageable);
    public int countByCompanyIdAndApply(String companyId, int apply);
    public Long countByCompanyId(String company);
    public Page<ConfirmEntity> findByCompanyId(String company,Pageable pageable);
    // 채용 확정 내역
    public Page<ConfirmEntity> findByCompanyIdAndApplyIn(String companyId, List<Integer> applies, Pageable pageable);
    public int countByCompanyIdAndApplyIn(String companyId, List<Integer> applies);
    public Long countByMemberIdAndCompanyIdNotIn(String memberId,List<String> companyId);
    public Long countByCompanyIdAndMemberIdNotIn(String memberId,List<String> companyId);
    public Page<ConfirmEntity> findByMemberIdAndCompanyIdNotIn(String memberId,List<String> companyId,Pageable pageable);
    public Page<ConfirmEntity> findByCompanyIdAndMemberIdNotIn(String memberId,List<String> companyId,Pageable pageable);
}
