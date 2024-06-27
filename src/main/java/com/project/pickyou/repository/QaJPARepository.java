package com.project.pickyou.repository;

import com.project.pickyou.entity.QaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QaJPARepository extends JpaRepository<QaEntity,Long> {
}
