package com.project.pickyou.repository;

import com.project.pickyou.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJPARepository extends JpaRepository<PaymentEntity,Long> {
}
