package com.project.pickyou.repository;

import com.project.pickyou.entity.QaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QaJPARepository extends JpaRepository<QaEntity,Long> {

    // 마지막 글 번호 가져오는 쿼리
    public Optional<QaEntity> findFirstByOrderByIdDesc();

    // qa 상세정보 보기
    public List<QaEntity> findByRef(int ref);
}
