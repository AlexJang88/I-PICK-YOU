package com.project.pickyou.repository;

import com.project.pickyou.entity.LicenceEntity;
import com.project.pickyou.entity.LicenceID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenceJPARepository extends JpaRepository<LicenceEntity, LicenceID> {
}
