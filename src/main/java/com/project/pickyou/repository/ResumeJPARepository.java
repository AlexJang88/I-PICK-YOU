package com.project.pickyou.repository;

import com.project.pickyou.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public interface ResumeJPARepository extends JpaRepository<ResumeEntity,Long> {
    public Optional<ResumeEntity> findByMemberIdAndRegType(String id, int type);
}
