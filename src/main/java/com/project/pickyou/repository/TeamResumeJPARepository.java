package com.project.pickyou.repository;

import com.project.pickyou.entity.TeamResumeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import java.util.List;
import java.util.Optional;

public interface TeamResumeJPARepository extends JpaRepository<TeamResumeEntity,Long> {
    public Optional<TeamResumeEntity> findFirstByOrderByIdDesc();
    @Query(value = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table", nativeQuery = true)
    Long getAutoIncrementValue(@Param("schema") String schema, @Param("table") String table);
    public Long countByStatus(int status);
    public Long countByMemberId(String memberId);
    public Page<TeamResumeEntity> findAllByStatus(int status, Pageable pageable);
    public Optional<TeamResumeEntity> findAllByMemberId(String id);

}
