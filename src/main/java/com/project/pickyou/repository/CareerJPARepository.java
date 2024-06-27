package com.project.pickyou.repository;

import com.project.pickyou.entity.CareerEntity;
import com.project.pickyou.entity.CareerID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerJPARepository extends JpaRepository<CareerEntity, CareerID> {
}
