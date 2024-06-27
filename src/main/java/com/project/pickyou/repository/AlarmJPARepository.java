package com.project.pickyou.repository;

import com.project.pickyou.entity.AlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmJPARepository extends JpaRepository<AlarmEntity,Long> {
}
