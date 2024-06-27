package com.project.pickyou.repository;

import com.project.pickyou.entity.EquipmentEntity;
import com.project.pickyou.entity.EquipmentID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentJPARepository extends JpaRepository<EquipmentEntity, EquipmentID> {
}
