package com.project.pickyou.repository;

import com.project.pickyou.entity.TeamResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamResumeJPARepository extends JpaRepository<TeamResumeEntity,Long> {
}
