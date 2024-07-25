package com.project.pickyou.service;

import com.project.pickyou.dto.PickDTO;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.entity.PickEntity;
import com.project.pickyou.entity.TeamResumeEntity;
import com.project.pickyou.repository.MemberJPARepository;
import com.project.pickyou.repository.PickJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PickServiceImpl implements PickService{
    private final PickJPARepository pickJPA;
    private final MemberJPARepository memberJPA;


    @Override
    public void myList(Model model, String id,int pageNum) {
        int type=0;
        int pageSize = 10;
        Long longCount = pickJPA.countByPicker(id);
        int count = longCount.intValue();
        Optional< MemberEntity> mem = memberJPA.findById(id);
        if(count>0) {
            Sort sort = Sort.by(Sort.Order.desc("target"));
            Page<PickEntity> page = pickJPA.findByPicker(id, PageRequest.of(pageNum - 1, pageSize, sort));
            List<PickEntity> posts = page.getContent();
            if(mem.isPresent()){
                if(mem.get().getAuth().contains("USER")){
                    type=1;
                }else if(mem.get().getAuth().contains("COMPANY")){
                    type=2;
                }
                System.out.println("---------------------------type"+type);
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
            model.addAttribute("type",type);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("startPage", startPage);
            model.addAttribute("pageBlock", pageBlock);
            model.addAttribute("endPage", endPage);
        }
    }

}
