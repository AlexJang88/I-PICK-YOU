package com.project.pickyou.repository;

import com.project.pickyou.entity.RecruitStateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitStateJPARepository extends JpaRepository<RecruitStateEntity,Long> {
    public Optional<RecruitStateEntity> findByRecruitIdAndMemberId(Long boardNum,String id);
    public Long countByRecruitIdAndMemberIdNotIn(Long recruitId,List<String> members);
    public Page<RecruitStateEntity> findByRecruitIdAndMemberIdNotIn(Long recruitId,List<String> members,Pageable page);

    // 나의 공고 지원 현황 가져오기
    public Page<RecruitStateEntity> findByMemberId(String memberId, Pageable pageable);

    // 나의 공고 지원 현황 카운트
    public int countByMemberId(String memberId);
}
