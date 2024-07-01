package com.project.pickyou.repository;

import com.project.pickyou.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageJPARepository extends JpaRepository<ImageEntity,Long> {

    List<ImageEntity> findByBoardNumAndBoardType(Long boardNum, int boardType); // 사진가져오기
    
}
