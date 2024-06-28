package com.project.pickyou.service;

import com.project.pickyou.dto.QaDTO;
import com.project.pickyou.entity.QaEntity;
import com.project.pickyou.repository.QaJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class QaServiceImpl implements QaService {

    private final QaJPARepository qaJPA;

    @Autowired
    public QaServiceImpl(QaJPARepository qaJPA) {
        this.qaJPA = qaJPA;
    }

    // qa 리스트 가져오기
    @Override
    public void qaList(Model model) {
        List<QaEntity> qaList = qaJPA.findAll();
        model.addAttribute("qaList", qaList);
    }

    // qa insert
    @Override
    public void qaInsert(QaDTO dto) {
        int refInt = 1;
        // 마지막 글번호 가져오기
        Optional<QaEntity> refLong = qaJPA.findFirstByOrderByIdDesc();
        if(refLong.isPresent()){
            Long ref = refLong.get().getId();
            refInt = ref.intValue() +1;
        }
        dto.setRef(refInt);

        // qa insert
        qaJPA.save(dto.toQaEntity());
    }

    // qa 상세정보 보기
    @Override
    public void qaInformation(Model model, int ref) {
        List<QaEntity> qaInforatmion = qaJPA.findByRef(ref);
        model.addAttribute("qaInforatmion", qaInforatmion);
    }

    /*
    // qa 상세정보 가져오기
    @Override
    public void qaInformation(Model model, Long id) {
        List<QaEntity> qaInformation = qaJPA.findAllById(id);
        model.addAttribute("qaInformation", qaInformation);
    }
    */
}
