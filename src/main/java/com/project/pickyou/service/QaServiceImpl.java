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

       // Page<QaEntity> page = qaJPA.findByIdEqualsRef(PageRequest.of(pageNum - 1, pageSize, sort));
        Page<QaEntity> page = qaJPA.qaList(PageRequest.of(pageNum - 1, pageSize, sort));
       // List<QaEntity> posts = qaJPA.findByIdEqualsRef();
        List<QaEntity> posts = page.getContent();

        // 이 부분
        // List<QaEntity> qaList = qaJPA.findAllGroupByRef();

        //model.addAttribute("qaList", qaList);
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

        /*
        // qa 리스트 가져오기
        List<QaEntity> qaList = qaJPA.findAll();
        // qa 리스트 ref 중복값 제거
        List<QaEntity> uniqueQaList = qaList.stream()
                // TreeSet을 이용하여 중복을 제거합니다. TreeSet은 중복 요소를 허용하지 않습니다.
                .collect(Collectors.collectingAndThen(
                        // TreeSet을 생성합니다. TreeSet은 Comparator를 통해 정렬 및 중복 제거를 수행합니다.
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(QaEntity::getRef))),
                        // TreeSet을 ArrayList로 변환합니다.
                        collected -> new ArrayList<>(collected)
                ));
        model.addAttribute("qaList", uniqueQaList);
        */


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

        System.out.println(qaInformation.get(0).getMemberId()+"-------------------------------------------------------------");
    }

    // qa 댓글 인서트
    @Override
    public void qaReplyInsert(QaDTO dto, int ref) {
        // qa 상세정보 보기
        List<QaEntity> qaInformation = qaJPA.findByRef(ref);
        // qa 첫번째 레코드값 가져오기
        QaEntity firstQaEntity = qaInformation.get(0);

        String memberId = firstQaEntity.getMemberId();
        String title = firstQaEntity.getTitle();

        dto.setMemberId(memberId);
        dto.setTitle(title);
        dto.setRef(ref);
        // qa 댓글 인서트
        qaJPA.save(dto.toQaEntity());
    }

    // qa 댓글 유무
    @Override
    public void qaReplyCount(int ref, Model model) {
        // qa 댓글 유무
        int count = qaJPA.countByRef(ref);
        model.addAttribute("count", count);
    }
}