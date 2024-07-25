package com.project.pickyou.service;

import com.project.pickyou.dto.QaDTO;
import com.project.pickyou.entity.QaEntity;
import com.project.pickyou.repository.QaJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.awt.print.Pageable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QaServiceImpl implements QaService {

    private final QaJPARepository qaJPA;

    @Autowired
    public QaServiceImpl(QaJPARepository qaJPA) {
        this.qaJPA = qaJPA;
    }

    // qa 리스트 가져오기, 페이징 처리
    @Override
    public void AllPosts(Model model, int pageNum) {
        int pageSize = 10;
        Long longCount = qaJPA.qaCount();
        int count = longCount.intValue();

        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<QaEntity> page = qaJPA.qaList(PageRequest.of(pageNum - 1, pageSize, sort));

        List<QaEntity> posts = page.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;   //페이징(이전/다음)을 몇개단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);

    }

    // qa insert
    @Override
    public void qaInsert(QaDTO dto) {

        String schema = "pickyou";
        String table = "qa";

        Long ref = qaJPA.getAutoIncrementValue(schema, table);
        int refInt = ref.intValue();
        dto.setRef(refInt);
        // qa insert
        qaJPA.save(dto.toQaEntity());
    }

    // qa 상세정보 보기
    @Override
    public void qaInformation(Model model, int ref) {
        // qa 상세정보 보기
        List<QaEntity> qaInformation = qaJPA.findByRef(ref);
        model.addAttribute("qaInformation", qaInformation);
    }

    // qa 댓글 인서트
    @Override
    public void qaReplyInsert(QaDTO dto, int ref) {
        // ref 값을 가진 모든 QaEntity를 조회
        List<QaEntity> qaInformation = qaJPA.findByRef(ref);

        if (qaInformation != null && !qaInformation.isEmpty()) {
            // 첫 번째 QaEntity를 가져와서 상태를 2로 변경
            QaEntity firstQaEntity = qaInformation.get(0);
            firstQaEntity.setStatus(2);

            // 변경된 상태를 저장
            qaJPA.save(firstQaEntity);

            // 댓글을 삽입하기 위해 DTO를 QaEntity로 변환
            QaEntity commentEntity = dto.toQaEntity();
            // 댓글의 ref를 기존 qaInformation의 ref로 설정
            commentEntity.setRef(ref);
            // 댓글의 상태를 기존 상태와 동일하게 유지
            commentEntity.setStatus(2); // 예를 들어, 댓글의 상태를 1로 설정

            // 댓글을 저장
            qaJPA.save(commentEntity);
        }
    }

    // qa 댓글 유무
    @Override
    public void qaReplyCount(int ref, Model model) {
        // qa 댓글 유무
        int count = qaJPA.countByRef(ref);
        model.addAttribute("count", count);
    }

    // qa 글삭제
    @Override
    public void qaDelete(long boardNum) {
        qaJPA.deleteById(boardNum);
    }
}