package com.project.pickyou.repository;

import com.project.pickyou.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointJPARepository extends JpaRepository<PointEntity,Long> {
}
