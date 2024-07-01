package com.project.pickyou.service;

import com.project.pickyou.entity.NoticeEntity;
import com.project.pickyou.repository.NoticeJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeJPARepository noticeJPA;

    @Autowired
    public NoticeServiceImpl(NoticeJPARepository noticeJPA) {
        this.noticeJPA = noticeJPA;
    }

    // 공지사항 리스트 가져오기
    @Override
    public void noticeList(Model model) {
        List<NoticeEntity> noticeList = noticeJPA.findAll();
        model.addAttribute("noticeList", noticeList);
    }
}