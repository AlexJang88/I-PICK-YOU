package com.project.pickyou.repository;

import com.project.pickyou.entity.SatisfactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SatisfactionJPARepository extends JpaRepository<SatisfactionEntity,Long> {
}
