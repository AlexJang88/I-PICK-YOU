package com.project.pickyou.repository;

import com.project.pickyou.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJPARepository extends JpaRepository<PaymentEntity,Long> {
    // 사업자 결제내역 가져오기
    public Page<PaymentEntity> findByMemberId(String memberId, Pageable pageable);

    public int countByMemberId(String memberId);
}
