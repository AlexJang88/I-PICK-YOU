package com.project.pickyou.repository;

import com.project.pickyou.entity.SatisfactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SatisfactionJPARepository extends JpaRepository<SatisfactionEntity,Long> {
    public Long countByWriterAndTarget(String writer,String Target);
    public Optional<SatisfactionEntity> findByWriterAndTarget(String writer,String Target);
    public List<SatisfactionEntity> findByTarget(String id);
    public List<SatisfactionEntity> findByTargetAndContentNotNull(String id);
    public List<SatisfactionEntity> findByWriter(String writer);
}
