package com.project.pickyou.service;

import com.project.pickyou.dto.ConfirmDTO;
import com.project.pickyou.dto.RecruitStateDTO;
import com.project.pickyou.entity.ConfirmEntity;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.entity.RecruitStateEntity;
import com.project.pickyou.repository.ConfirmJPARepository;
import com.project.pickyou.repository.MemberJPARepository;
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
    private final MemberJPARepository memberJPA;



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
       Long longCount =0L;
       int count=0;
        Sort sort = Sort.by(Sort.Order.desc("reg"));
        memberlist = confirmJPA.findByRecruitIdAndApplyNot(recruitId,type);
        Page<RecruitStateEntity> page = Page.empty();
       if(!CollectionUtils.isEmpty(memberlist)){
           for(ConfirmEntity ce:memberlist){
               members.add(ce.getMemberId());
           }
           longCount = recruitStateJPA.countByRecruitIdAndMemberIdNotIn(recruitId,members);
           count = longCount.intValue();
           page =recruitStateJPA.findByRecruitIdAndMemberIdNotIn(recruitId,members,PageRequest.of(pageNum - 1, pageSize, sort));
       }else{
           longCount = recruitStateJPA.countByRecruitId(recruitId);
           count= longCount.intValue();
           page=recruitStateJPA.findByRecruitId(recruitId,PageRequest.of(pageNum - 1, pageSize, sort));
       }
        System.out.println("=====================count"+members);
        System.out.println("=====================count"+count);


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
    public int cancelConfirmed(String memberId,Long recruitId,int type) {
        int check=0;
        ConfirmDTO confirm = new ConfirmDTO();
        Optional<ConfirmEntity> ce =confirmJPA.findByMemberIdAndRecruitIdAndApplyNot(memberId,recruitId,type);
        if(ce.isEmpty()) {
            System.out.println("---------------ce" + ce.get().getId());
        }
        Optional<MemberEntity> mem = memberJPA.findById(memberId);
        System.out.println("---------------mem"+mem.get().getId());
        if(ce.isPresent()){
            confirm=ce.get().toConfirmDTO();
            confirm.setApply(3);
            System.out.println("---------------dto"+confirm);
            System.out.println("---------------check"+check);
            confirmJPA.save(confirm.toConfirmEntity());
        }
        return check;
    }

    @Override
    public void myApply(Model model, String member, int pageNum) {
        int pageSize = 10;
        List<RecruitStateEntity> posts = Collections.emptyList();
        Long longCount = recruitStateJPA.countByMemberId(member);
        int count = longCount.intValue();
        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<RecruitStateEntity> page=recruitStateJPA.findByMemberId(member,PageRequest.of(pageNum - 1, pageSize, sort));
        //  로그인 처리후 사용할 코드
        if(!page.isEmpty()) {
            posts = page.getContent();
            System.out.println("==============page check"+page);
        }
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
    public void Apply(String member, Long boardNum) {
        RecruitStateDTO dto = new RecruitStateDTO();
        dto.setMemberId(member);
        dto.setRecruitId(boardNum);
        System.out.println("><><><><"+dto);
        recruitStateJPA.save(dto.toRecruitStateEntity());
    }

    @Override
    public int cancelApply(String member, Long boardNum) {
        int check=0;
        Optional<RecruitStateEntity> rse=recruitStateJPA.findByRecruitIdAndMemberId(boardNum,member);
        Optional<MemberEntity> mem = memberJPA.findById(member);
        if(rse.isPresent()){
            if(mem.isPresent()){
                if(mem.get().getAuth().contains("ADMIN")){
                    System.out.println("---------------ADMIN"+mem.get().getAuth());
                    check=99;
                } else if (mem.get().getAuth().contains("USER")) {
                    System.out.println("---------------ADMIN"+mem.get().getAuth());
                    check=1;
                } else if (mem.get().getAuth().contains("COMPANY")) {
                    System.out.println("---------------ADMIN"+mem.get().getAuth());
                    check=2;
                }
            }
            recruitStateJPA.deleteById(rse.get().getId());
        }

        return check;
    }
}
