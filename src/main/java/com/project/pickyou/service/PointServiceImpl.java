package com.project.pickyou.service;

import com.project.pickyou.entity.NoticeEntity;
import com.project.pickyou.entity.PointEntity;
import com.project.pickyou.repository.PointJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {

    private final PointJPARepository pointJPA;

    // 포인트 차감 내역 가져오기
    @Override
    public void AllPosts(Model model, int pageNum, String memberId) {
        int pageSize = 10;
        int count = pointJPA.countByMemberId(memberId);
        //int count = longCount.intValue();

        Sort sort = Sort.by(Sort.Order.desc("reg"));

        Page<PointEntity> page = pointJPA.findByMemberId(memberId, (Pageable) PageRequest.of(pageNum - 1, pageSize, sort));

        List<PointEntity> posts = page.getContent();

        int totalPoints = 0;
        for (PointEntity post : posts) {
            if (post.getStatus() == 1) {
                totalPoints += post.getPoint();
            } else if (post.getStatus() == 2) {
                totalPoints -= post.getPoint();
            }
        }

        model.addAttribute("totalPoints", totalPoints);
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
}
