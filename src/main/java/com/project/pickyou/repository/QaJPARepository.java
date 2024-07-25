package com.project.pickyou.repository;

import com.project.pickyou.entity.QaEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QaJPARepository extends JpaRepository<QaEntity,Long> {

    // 현재 시퀀스의 값 가져오기
    @Query(value = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table", nativeQuery = true)
    Long getAutoIncrementValue(@Param("schema") String schema, @Param("table") String table);

    // qa 중복제거된 리스트
    @Query("SELECT q FROM QaEntity q WHERE q.id = q.ref")
    public Page<QaEntity> qaList(Pageable pageable);

    // qa 중복제거된 카운트
    @Query("SELECT COUNT(q) FROM QaEntity q WHERE q.id = q.ref")
    public long qaCount();






    // qa 상세정보 보기
    public List<QaEntity> findByRef(int ref);

    // qa 댓글 유무
    public int countByRef(int ref);





    // 중복값 제거하고 list 가져오기
    //@Query("SELECT * FROM QaEntity GROUP BY ref")
    //public Page<QaEntity> findAllByGroupByRef(Pageable pageable, Sort sort);
    //public List<QaEntity> findByIdEqualsRef();
   // public Page<QaEntity> qaList(Pageable pageable, Sort sort);
}
