package com.project.pickyou.repository;

import com.project.pickyou.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface NoticeJPARepository extends JpaRepository<NoticeEntity,Long> {
    // 공지사항 상세정보 가져오기
    public Optional<NoticeEntity> findById(Long id);
}


