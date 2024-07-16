package com.project.pickyou.repository;

import com.project.pickyou.entity.PointEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PointJPARepository extends JpaRepository<PointEntity,Long> {

    public int countByMemberId(String memberId);

    // 사용자가 사용한 내역
    public Page<PointEntity> findByMemberId(String memberId, Pageable pageable);


    // 모든 사용자 포인트 적립 리스트
    public Page<PointEntity> findByStatus(int status, Pageable pageable);

    public int countByStatus(int status);
}

