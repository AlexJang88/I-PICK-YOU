package com.project.pickyou.repository;

import com.project.pickyou.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJPARepository extends JpaRepository<MemberEntity,String> {

    boolean existsById(String id);

    MemberEntity findByid(String id);   //회원인지 아닌지 확인절차



}
