package com.project.pickyou.repository;

import com.project.pickyou.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResumeJPARepository extends JpaRepository<ResumeEntity,Long> {
    public Optional<ResumeEntity> findByMemberIdAndRegType(String id,int type);

    // 이력서 현재 시퀀스값 +1 가져오기
    @Query(value = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table", nativeQuery = true)
    Long getAutoIncrementValue(@Param("schema") String schema, @Param("table") String table);

    // 이력서 리스트 가져오기
    public List<ResumeEntity> findByMemberIdOrderByRegDesc(String memberId);

    // 이력서 상세정보
    public Optional<ResumeEntity> findById(Long num);

    // 이력서 공개/비공개
    public Optional<ResumeEntity> findByRegTypeAndMemberId(int regType, String memberId);
}
