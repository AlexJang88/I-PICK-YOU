package com.project.pickyou.repository;

import com.project.pickyou.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageJPARepository extends JpaRepository<ImageEntity,Long> {
    public List<ImageEntity> findByBoardTypeAndBoardNum(int boardType, Long boardNum);
    @Transactional
    public void deleteAllByBoardTypeAndBoardNum(int boardType,Long boardNum);
}
