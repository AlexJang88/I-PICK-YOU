package com.project.pickyou.repository;

import com.project.pickyou.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJPARepository extends JpaRepository<MemberEntity,String> {
}
