package com.project.pickyou.repository;

import com.project.pickyou.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageJPARepository extends JpaRepository<ImageEntity,Long> {

    // 타입, 글번호에 맞는 사진가져오기 (공지사항)
    List<ImageEntity> findByBoardNumAndBoardType(Long boardNum, int boardType);

    // 글번호에 맞는 사진 삭제하기
    @Transactional
    public void deleteAllByBoardTypeAndBoardNum(int boardType, Long boardNum);

    public List<ImageEntity> findByBoardTypeAndBoardNum(int boardType, Long boardNum);
}
