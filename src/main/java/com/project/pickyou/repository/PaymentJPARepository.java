package com.project.pickyou.repository;

import com.project.pickyou.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJPARepository extends JpaRepository<PaymentEntity,Long> {
    // 사업자 결제내역 가져오기
    public Page<PaymentEntity> findByMemberIdAndPointHistory(String memberId, int pointHistory, Pageable pageable);

    public int countByMemberIdAndPointHistory(String memberId, int pointHistory);

    // 테스트 ///////
    // 결제, 포인트 사용내역 가져오기
    public Page<PaymentEntity> findByPointHistory(int pointHistory, Pageable pageable);
    public int countByPointHistory(int pointHistory);
    // 테스트 ///////
}
