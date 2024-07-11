package com.project.pickyou.service;

import com.project.pickyou.entity.RecruitStateEntity;
import org.springframework.ui.Model;

import java.util.List;

public interface RecruitStateService {
    public void confirmedMember(Model model,int pageNum,Long recuritId);
    public void onApply(Model model,int pageNum,Long recuritId);
    public void cancelConfirmed(String memberId,Long recuritId);
}
