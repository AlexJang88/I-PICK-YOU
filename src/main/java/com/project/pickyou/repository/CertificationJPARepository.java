package com.project.pickyou.repository;

import com.project.pickyou.entity.CertificationEntity;
import com.project.pickyou.entity.CertificationID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationJPARepository extends JpaRepository<CertificationEntity, CertificationID> {
}
