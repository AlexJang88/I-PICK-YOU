package com.project.pickyou.repository;

import com.project.pickyou.dto.MemberDTO;
import com.project.pickyou.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberJPARepository extends JpaRepository<MemberEntity,String> {

    boolean existsById(String id);  //아이디 중복체크
    boolean existsByEmail(String email);   //이메일 중복체크
    Optional<MemberEntity> findById(String id);//회원인지 아닌지 확인절차


    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByIdAndEmail(String id, String email);


    List<MemberEntity> findByAuth(String business);  //권한이 사업자인사람뽑기

}
