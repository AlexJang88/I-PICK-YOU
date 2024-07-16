package com.project.pickyou.repository;

import com.project.pickyou.entity.AlarmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmJPARepository extends JpaRepository<AlarmEntity,Long> {

    Long countByReaderId(String id);


    Page<AlarmEntity> findByReaderId(String id, Pageable pageSize); //페이지 갯수에 맞게 가져오기

    AlarmEntity findByIdAndReaderId(Long num, String id); //번호와 이름에 맞는 상세내용찾기

    Long countByMemberId(String id);  //관리자가 보낸 쪽지 갯수

    Page<AlarmEntity> findByMemberId(String id, Pageable pageSize); //관리자가 보낸 쪽지 내역 리스트화

    AlarmEntity findByIdAndMemberId(Long num, String id); //관리자가 보낸 쪽지 상세내용

    Long countByReaderIdAndStatus(String id, int read);  //에이젝스로 받은 숫자 (안읽은 갯수)

}
