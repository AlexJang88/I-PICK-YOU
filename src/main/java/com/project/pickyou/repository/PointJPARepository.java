package com.project.pickyou.repository;

import com.project.pickyou.entity.PointEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface PointJPARepository extends JpaRepository<PointEntity,Long> {

    public int countByMemberIdAndStatus(String memberId, int status);

    // 사용자가 사용한 내역
    public Page<PointEntity> findByMemberIdAndStatus(String memberId, int status, Pageable pageable);

    // 모든 사용자 포인트 적립 리스트
    public Page<PointEntity> findByStatus(int status, Pageable pageable);

    public int countByStatus(int status);

    @Query("SELECT SUM(p.point) FROM PointEntity p WHERE p.memberId = :memberId AND p.status = :status")
    Integer findTotalPointByMemberIdAndStatus(@Param("memberId") String memberId, @Param("status") int status);  //본인의 포인트 전체 합산값

}
