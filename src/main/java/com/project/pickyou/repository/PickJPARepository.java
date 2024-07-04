package com.project.pickyou.repository;

import com.project.pickyou.entity.PickEntity;
import com.project.pickyou.entity.PickID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickJPARepository extends JpaRepository<PickEntity, PickID> {

}
