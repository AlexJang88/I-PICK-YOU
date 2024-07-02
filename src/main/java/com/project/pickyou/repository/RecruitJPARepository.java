package com.project.pickyou.repository;

import com.project.pickyou.entity.RecruitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitJPARepository extends JpaRepository<RecruitEntity,Long> {
    @Query(value = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table", nativeQuery = true)
    Long getAutoIncrementValue(@Param("schema") String schema, @Param("table") String table);
}
