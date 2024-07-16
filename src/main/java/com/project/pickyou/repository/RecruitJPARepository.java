package com.project.pickyou.repository;

import com.project.pickyou.entity.RecruitEntity;
import com.project.pickyou.entity.RecruitStateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitJPARepository extends JpaRepository<RecruitEntity,Long> {
    @Query(value = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table", nativeQuery = true)
    Long getAutoIncrementValue(@Param("schema") String schema, @Param("table") String table);
    public Long countByStatus(int status);
    public Page<RecruitEntity> findByStatus(int status,Pageable page);
    public Long countByMemberId(String memberId);
    public Page<RecruitEntity> findByMemberId(String memberId, Pageable page);
    public Page<RecruitEntity> findById(String id,Pageable page);

}
