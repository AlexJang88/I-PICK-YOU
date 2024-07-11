package com.project.pickyou.service;

import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.entity.SatisfactionEntity;
import com.project.pickyou.repository.ConfirmJPARepository;
import com.project.pickyou.repository.MemberJPARepository;
import com.project.pickyou.repository.SatisfactionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SatisfactionServiceImpl implements SatisfactionService{
    
    private final ConfirmJPARepository confirmJPA;
    private final MemberJPARepository memberJPA;
    private final SatisfactionJPARepository satisfactionJPA;
    
    @Override
    public int existCheck(String writer, String target,Model model) {
        int result=3;
        Long count=0L;
        Long scoreCount=0L;
        MemberEntity member = null;
        MemberEntity company =null;
        Optional<MemberEntity> writerInfo=memberJPA.findById(writer);
        if(writerInfo.isPresent()){
            if(writerInfo.get().getAuth().contains("COMPANY")){
                count=confirmJPA.countByMemberIdAndCompanyId(target,writer);
                if(count>0){
                    scoreCount=satisfactionJPA.countByWriterAndTarget(target,writer);
                    if(scoreCount>0){
                        Optional<SatisfactionEntity> info=satisfactionJPA.findByWriterAndTarget(target,writer);
                        Optional<MemberEntity> mem=memberJPA.findById(target);
                        member=mem.get();
                        model.addAttribute("member",member);
                        model.addAttribute("company",company);
                        model.addAttribute("post",info.get());
                        result=2;
                    }else{
                        Optional<MemberEntity> mem=memberJPA.findById(target);
                        model.addAttribute("member",member);
                        model.addAttribute("company",company);
                        result=1;
                    }
                }
            } else if (writerInfo.get().getAuth().contains("USER")) {
                count=confirmJPA.countByMemberIdAndCompanyId(writer,target);
                if(count>0){
                    scoreCount=satisfactionJPA.countByWriterAndTarget(writer,target);
                    if(scoreCount>0){
                        Optional<SatisfactionEntity> info=satisfactionJPA.findByWriterAndTarget(target,writer);
                        Optional<MemberEntity> mem=memberJPA.findById(target);
                        company=mem.get();
                        model.addAttribute("member",member);
                        model.addAttribute("company",company);
                        model.addAttribute("post",info.get());
                        result=2;
                    }else{
                        model.addAttribute("member",member);
                        model.addAttribute("company",company);
                        result=1;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void myScore(Model model, String id) {
        int sum=0;
        double davg=0;
        int avg=0;
        List<SatisfactionEntity> scoreList = Collections.emptyList();
        List<SatisfactionEntity> contentList = Collections.emptyList();
        Optional<MemberEntity> member = memberJPA.findById(id);
        if(member.isPresent()){
        if(member.get().getId().contains("COMPANY")){
            model.addAttribute("company",member.get());
        }else if(member.get().getId().contains("USER")){
            model.addAttribute("member",member.get());
        }
        }
        if(!CollectionUtils.isEmpty(satisfactionJPA.findByTarget(id))) {
            scoreList = satisfactionJPA.findByTarget(id);
        }
        for(SatisfactionEntity se:scoreList){
            sum+=se.getScore();
        }
        davg = Math.round((double)sum/scoreList.size());
        avg=Double.valueOf(avg).intValue();
        if(!CollectionUtils.isEmpty(satisfactionJPA.findByTargetAndContentNotNull(id))){
            contentList = satisfactionJPA.findByTargetAndContentNotNull(id);
        }
        model.addAttribute("avg",avg);
        model.addAttribute("posts",contentList);


    }
}
