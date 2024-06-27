package com.project.pickyou.repository;

import com.project.pickyou.entity.JobEntity;
import com.project.pickyou.entity.JobID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobJPARepository extends JpaRepository<JobEntity, JobID> {
}
