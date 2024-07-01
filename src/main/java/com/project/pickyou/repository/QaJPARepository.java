package com.project.pickyou.repository;

import com.project.pickyou.entity.QaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QaJPARepository extends JpaRepository<QaEntity,Long> {

    // 현재 시퀀스의 값 가져오기
    @Query(value = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table", nativeQuery = true)
    Long getAutoIncrementValue(@Param("schema") String schema, @Param("table") String table);

    // qa 상세정보 보기
    public List<QaEntity> findByRef(int ref);

    // qa 댓글 유무
    public int countByRef(int ref);
}
