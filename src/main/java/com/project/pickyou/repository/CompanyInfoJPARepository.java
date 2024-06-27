package com.project.pickyou.repository;

import com.project.pickyou.entity.CompanyInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyInfoJPARepository extends JpaRepository<CompanyInfoEntity,String> {
}
