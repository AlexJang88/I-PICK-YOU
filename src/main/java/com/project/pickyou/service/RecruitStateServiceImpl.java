package com.project.pickyou.service;

import com.project.pickyou.dto.ConfirmDTO;
import com.project.pickyou.entity.ConfirmEntity;
import com.project.pickyou.entity.RecruitStateEntity;
import com.project.pickyou.repository.ConfirmJPARepository;
import com.project.pickyou.repository.RecruitJPARepository;
import com.project.pickyou.repository.RecruitStateJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitStateServiceImpl implements RecruitStateService {
    private final RecruitStateJPARepository recruitStateJPA;
    private final RecruitJPARepository recruitJPA;
    private final ConfirmJPARepository confirmJPA;



    @Override
    public void confirmedMember(Model model, int pageNum, Long recruitId,Integer type) {
        int pageSize = 10;
        Long longCount = confirmJPA.countByRecruitIdAndApplyNot(recruitId,type);
        int count = longCount.intValue();
        Sort sort = Sort.by(Sort.Order.desc("id"));
       Page<ConfirmEntity> page = null;
       List<ConfirmEntity> posts=Collections.emptyList();
       System.out.println("=========count"+count);
        if (count > 0) {
            page = confirmJPA.findByRecruitIdAndApplyNot(recruitId,type, PageRequest.of(pageNum - 1, pageSize, sort));
            if (page != null) {
                posts = page.getContent();
            }
            System.out.println("===========null" + posts);
        }
        //  로그인 처리후 사용할 코드


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

    @Override
    public void onApply(Model model,int pageNum, Long recruitId,Integer type) {
        int pageSize = 10;
       List<RecruitStateEntity> posts = Collections.emptyList();
       List<ConfirmEntity> memberlist = Collections.emptyList();
       List<String> members = Collections.emptyList();
        memberlist = confirmJPA.findByRecruitIdAndApplyNot(recruitId,type);
       if(CollectionUtils.isEmpty(memberlist)){
           for(ConfirmEntity ce:memberlist){
               members.add(ce.getMemberId());
           }
       }
        System.out.println("=====================count"+members);
        Long longCount = recruitStateJPA.countByRecruitIdAndMemberIdNotIn(recruitId,members);
        int count = longCount.intValue();
        System.out.println("=====================count");
        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<RecruitStateEntity> page=recruitStateJPA.findByRecruitIdAndMemberIdNotIn(recruitId,members,PageRequest.of(pageNum - 1, pageSize, sort));
        //  로그인 처리후 사용할 코드
        if(!page.isEmpty()) {
            posts = page.getContent();
            System.out.println("==============page check"+page);
        }
        System.out.println("=============="+posts);

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

    @Override
    @Transactional
    public void cancelConfirmed(String memberId,Long recruitId,Integer type) {
        ConfirmDTO confirm = new ConfirmDTO();
        Optional<ConfirmEntity> ce =confirmJPA.findByMemberIdAndRecruitIdAndApplyNot(memberId,recruitId,type);
        if(ce.isPresent()){
            confirm=ce.get().toConfirmDTO();
            confirm.setApply(3);
            confirmJPA.save(confirm.toConfirmEntity());
        }
    }
}
