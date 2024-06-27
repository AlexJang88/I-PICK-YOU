package com.project.pickyou.repository;

import com.project.pickyou.entity.FoodMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodMapJPARepository extends JpaRepository<FoodMapEntity,Long> {
}
