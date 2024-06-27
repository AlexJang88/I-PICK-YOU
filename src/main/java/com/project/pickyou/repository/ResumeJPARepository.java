package com.project.pickyou.repository;

import com.project.pickyou.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeJPARepository extends JpaRepository<ResumeEntity,Long> {
}
