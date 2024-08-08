package com.project.pickyou.service;

import com.project.pickyou.dto.SatisfactionDTO;
import com.project.pickyou.entity.ConfirmEntity;
import com.project.pickyou.entity.EducationEntity;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.entity.SatisfactionEntity;
import com.project.pickyou.repository.ConfirmJPARepository;
import com.project.pickyou.repository.MemberJPARepository;
import com.project.pickyou.repository.SatisfactionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SatisfactionServiceImpl implements SatisfactionService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final ConfirmJPARepository confirmJPA;
    private final MemberJPARepository memberJPA;
    private final SatisfactionJPARepository satisfactionJPA;

    @Override
    public int existCheck(String writer, String target, Model model) {
        model.addAttribute("bucketName",bucket);
        model.addAttribute("regionName",region);
        int result = 3;
        Long count = 0L;
        Long scoreCount = 0L;
        MemberEntity member = null;
        MemberEntity company = null;
        Optional<MemberEntity> writerInfo = memberJPA.findById(writer);
        if (writerInfo.isPresent()) {
            if (writerInfo.get().getAuth().contains("COMPANY")) {
                count = confirmJPA.countByMemberIdAndCompanyId(target, writer);
                if (count > 0) {
                    scoreCount = satisfactionJPA.countByWriterAndTarget(target, writer);
                    if (scoreCount > 0) {
                        Optional<SatisfactionEntity> info = satisfactionJPA.findByWriterAndTarget(target, writer);
                        Optional<MemberEntity> mem = memberJPA.findById(target);
                        member = mem.get();
                        model.addAttribute("member", member);
                        model.addAttribute("company", company);
                        model.addAttribute("post", info.get());
                        result = 2;
                    } else {
                        Optional<MemberEntity> mem = memberJPA.findById(target);
                        member=mem.get();
                        model.addAttribute("member", member);
                        model.addAttribute("company", company);
                        result = 1;
                    }
                }
            } else if (writerInfo.get().getAuth().contains("USER")) {
                count = confirmJPA.countByMemberIdAndCompanyId(writer, target);
                if (count > 0) {
                    scoreCount = satisfactionJPA.countByWriterAndTarget(writer, target);
                    if (scoreCount > 0) {
                        Optional<SatisfactionEntity> info = satisfactionJPA.findByWriterAndTarget(target, writer);
                        Optional<MemberEntity> mem = memberJPA.findById(target);
                        company = mem.get();
                        model.addAttribute("member", member);
                        model.addAttribute("company", company);
                        model.addAttribute("post", info.get());
                        result = 2;
                    } else {
                        Optional<MemberEntity> mem = memberJPA.findById(target);
                        company = mem.get();
                        model.addAttribute("member", member);
                        model.addAttribute("company", company);
                        result = 1;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void myScore(Model model, String id,int type) {
        int sum = 0;
        double davg = 0.0;
        int avg = 0;
        List<SatisfactionEntity> scoreList = Collections.emptyList();
        List<SatisfactionEntity> contentList = Collections.emptyList();
        Optional<MemberEntity> userInfo = memberJPA.findById(id);
        if (userInfo.isPresent()) {
            MemberEntity member = null;
            MemberEntity company = null;
            if (userInfo.get().getAuth().contains("COMPANY")) {
                company = userInfo.get();
                model.addAttribute("company", company);
                model.addAttribute("member", member);
            } else if (userInfo.get().getAuth().contains("USER")) {
                member = userInfo.get();
                model.addAttribute("company", company);
                model.addAttribute("member", member);
            }
        }
        if (!CollectionUtils.isEmpty(satisfactionJPA.findByTarget(id))) {
            scoreList = satisfactionJPA.findByTarget(id);
            for (SatisfactionEntity se : scoreList) {
                sum += se.getScore();
            }
            davg = (double) sum/scoreList.size();
            avg = (int) Math.round(davg);
        }

        if (!CollectionUtils.isEmpty(satisfactionJPA.findByTargetAndContentNotNull(id))) {
            contentList = satisfactionJPA.findByTargetAndContentNotNull(id);
        }
        model.addAttribute("avg", avg);
        model.addAttribute("bucketName",bucket);
        model.addAttribute("regionName",region);
        if(type==1) {
            model.addAttribute("posts", contentList);
        }


    }

    @Override
    public void scoreSet(SatisfactionDTO dto) {
        satisfactionJPA.save(dto.toSatisfactionEntity());
    }

    @Override
    public void scoreEdit(SatisfactionDTO dto) {
        satisfactionJPA.save(dto.toSatisfactionEntity());
    }

    @Override
    public void scoreList(Model model, String sid,int pageNum) {
        int type=0;
        int auth=0;
        int pageSize = 10;
        int count =0;
        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Optional<MemberEntity> member = memberJPA.findById(sid);
        Page<ConfirmEntity> page = Page.empty();
        List<ConfirmEntity> posts = Collections.emptyList();


        List<SatisfactionEntity> sfList = Collections.emptyList();
                sfList=satisfactionJPA.findByWriter(sid);

        List<String> targetList=new ArrayList<>();
        if(!sfList.isEmpty()) {
            for (SatisfactionEntity sfe : sfList) {
                targetList.add(sfe.getTarget());

            }
        }
            if(member.isPresent()) {
                if(member.get().getAuth().contains("USER")) {
                    if(!targetList.isEmpty()) {
                        Long longcount = confirmJPA.countByMemberIdAndCompanyIdNotIn(sid, targetList);
                    count = longcount.intValue();
                    page = confirmJPA.findByMemberIdAndCompanyIdNotIn(sid,targetList,PageRequest.of(pageNum - 1, pageSize, sort));
                    posts=page.getContent();
                    }else{
                        count=confirmJPA.countByMemberId(sid);
                        page = confirmJPA.findByMemberId(sid,PageRequest.of(pageNum - 1, pageSize, sort));
                        posts=page.getContent();
                    }
                    auth=1;
                }
                else if (member.get().getAuth().contains("COMPANY")) {
                    if(!targetList.isEmpty()) {
                        Long longcount = confirmJPA.countByCompanyIdAndMemberIdNotIn(sid, targetList);
                        count = longcount.intValue();
                        page = confirmJPA.findByCompanyIdAndMemberIdNotIn(sid, targetList, PageRequest.of(pageNum - 1, pageSize, sort));
                        posts = page.getContent();
                    }else{
                        Long longcount = confirmJPA.countByCompanyId(sid);
                        count = longcount.intValue();
                        page= confirmJPA.findByCompanyId(sid,PageRequest.of(pageNum - 1, pageSize, sort));
                        posts=page.getContent();
                    }
                    auth=2;
                }
            }
        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;   //페이징(이전/다음)을 몇개단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }

        model.addAttribute("auth",auth);
        model.addAttribute("posts", posts);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
    }

}
