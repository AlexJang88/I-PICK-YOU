package com.project.pickyou.repository;

import com.project.pickyou.entity.FoodMapEntity;
import com.project.pickyou.entity.QaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodMapJPARepository extends JpaRepository<FoodMapEntity,Long> {

    // 현재 시퀀스의 값 가져오기
    @Query(value = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table", nativeQuery = true)
    Long getAutoIncrementValue(@Param("schema") String schema, @Param("table") String table);

    // 푸드맵 상세정보 보기
    public List<FoodMapEntity> findByRef(int ref);

    // 푸드맵 리스트 가져오기
    @Query("SELECT f FROM FoodMapEntity f WHERE f.id = f.ref")
    Page<FoodMapEntity> foodMapList(Pageable pageable);

    // 푸드맵 리스트 중복제거된 카운트
    @Query("SELECT COUNT(f) FROM FoodMapEntity f WHERE f.id = f.ref")
    public long fmCount();

    Optional<FoodMapEntity> findByIdAndRef(Long id, int ref);
}
