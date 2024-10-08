package com.project.pickyou.repository;

import com.project.pickyou.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface NoticeJPARepository extends JpaRepository<NoticeEntity,Long> {
    // 공지사항 상세정보 가져오기
    public Optional<NoticeEntity> findById(Long id);

    // 메인에 공지사항 1개만 나오게
    public Optional<NoticeEntity> findTopByOrderByRegDesc();

    @Query(value = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table", nativeQuery = true)
    Long getAutoIncrementValue(@Param("schema") String schema, @Param("table") String table);

}


