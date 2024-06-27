package com.project.pickyou.repository;

import com.project.pickyou.entity.RecruitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitJPARepository extends JpaRepository<RecruitEntity,Long> {
}
