package com.project.pickyou.repository;

import com.project.pickyou.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJPARepository extends JpaRepository<ImageEntity,Long> {
}
