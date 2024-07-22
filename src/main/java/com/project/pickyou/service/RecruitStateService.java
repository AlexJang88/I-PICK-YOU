package com.project.pickyou.service;

import com.project.pickyou.entity.RecruitStateEntity;
import org.springframework.ui.Model;

import java.util.List;

public interface RecruitStateService {
    public void confirmedMember(Model model,int pageNum,Long recuritId,Integer type);
    public void onApply(Model model,int pageNum,Long recuritId,int type);
    public int cancelConfirmed(String memberId,Long recuritId,int type);
    public void myApply(Model model,String member,int pageNum);
    public void Apply(String member,Long boardNum);
    public int cancelApply(String member,Long boardNum);
}
