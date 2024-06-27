package com.project.pickyou.repository;

import com.project.pickyou.entity.ImageRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRegistrationJPARepository extends JpaRepository<ImageRegistrationEntity,Long> {
}
