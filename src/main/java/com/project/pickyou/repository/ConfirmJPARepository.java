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
   public List<ConfirmEntity> findByCompanyId(String companyId);
    // 유저 입장 채용현황
    public Page<ConfirmEntity> findByMemberId(String memberId, Pageable pageable);
    public List<ConfirmEntity> findByMemberId(String memberId);
    public int countByMemberId(String memberId);

    // 고용 요청 내역(사업자가 요청한것)
    public Page<ConfirmEntity> findByCompanyIdAndApply(String companyId, int apply, Pageable pageable);
    public int countByCompanyIdAndApply(String companyId, int apply);

    // 채용 확정 내역
    public Page<ConfirmEntity> findByCompanyIdAndApplyIn(String companyId, List<Integer> applies, Pageable pageable);
    public int countByCompanyIdAndApplyIn(String companyId, List<Integer> applies);
}
