package com.project.pickyou.repository;

import com.project.pickyou.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeJPARepository extends JpaRepository<NoticeEntity,Long> {
}
