package com.project.pickyou.service;

import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.entity.PaymentEntity;
import com.project.pickyou.entity.PointEntity;
import com.project.pickyou.entity.TrainningEntity;
import com.project.pickyou.repository.MemberJPARepository;
import com.project.pickyou.repository.PaymentJPARepository;
import com.project.pickyou.repository.PointJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final PointJPARepository pointJPA;
    private final MemberJPARepository memberJPARepository;
    private final PaymentJPARepository paymentJPA;

    @Override
    public void getUsre(Model model, int pageNum) { //일반회원정보가져오기
        int pageSize = 10;
        String auth = "ROLE_USER";
        Long longCount =  memberJPARepository.countByAuth(auth);// 일반총인원

        int count =longCount.intValue(); //타입변환
        Sort sort = Sort.by(Sort.Order.desc("reg")); //내림차순

        Page<MemberEntity> page = memberJPARepository.findByAuth(auth, PageRequest.of(pageNum - 1, pageSize, sort));

        List<MemberEntity> getUsrelist = page.getContent();

        model.addAttribute("getUsrelist", getUsrelist);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum - 1) / 10 * 10 + 1;
        int pageBlock = 10;  // 페이징(이전/다음)을 몇 개 단위로 끊을지
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
    public void userDelete(String id) { //일반, 사업자 유저 삭제
        memberJPARepository.deleteById(id);
    }

    @Override
    public void getCompany(Model model, int pageNum) { //사업자 정보가져오기
        int pageSize = 10;
        String auth = "ROLE_COMPANY";
        Long longCount =  memberJPARepository.countByAuth(auth);// 일반총인원

        int count =longCount.intValue(); //타입변환
        Sort sort = Sort.by(Sort.Order.desc("reg")); //내림차순

        Page<MemberEntity> page = memberJPARepository.findByAuth(auth, PageRequest.of(pageNum - 1, pageSize, sort));

        List<MemberEntity> getUsrelist = page.getContent();

        model.addAttribute("getCompanylist", getUsrelist);
        model.addAttribute("count", count);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum - 1) / 10 * 10 + 1;
        int pageBlock = 10;  // 페이징(이전/다음)을 몇 개 단위로 끊을지
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }

        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
    }


    // 포인트 지급, 사용내역
    @Override
    public void AllPostsGIVE(Model model, int status, int pageNum, int month) {
        int pageSize = 10;
        int count = pointJPA.countByStatus(status);

        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<PointEntity> page = pointJPA.findByStatus(status, PageRequest.of(pageNum - 1, pageSize, sort));

        List<PointEntity> allPosts = page.getContent();

        // Filter posts by month
        List<PointEntity> filteredPosts = allPosts.stream()
                .filter(post -> {
                    // Convert Date to LocalDate
                    LocalDate regDate = post.getReg().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    // Compare month values
                    return regDate.getMonthValue() == month;
                })
                .collect(Collectors.toList());

        int filteredCount = filteredPosts.size();

        model.addAttribute("posts", filteredPosts);
        model.addAttribute("count", filteredCount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        int pageCount = filteredCount / pageSize + (filteredCount % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
        model.addAttribute("month", month); // 추가: 현재 월을 모델에 추가
    }


    // 결제, 포인트 사용내역 가져오기
    @Override
    public void AllpaymentANDpoint(Model model, int pointHistory, int pageNum, int month) {
        int pageSize = 10;
        int count = paymentJPA.countByPointHistory(pointHistory);

        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<PaymentEntity> page = paymentJPA.findByPointHistory(pointHistory, PageRequest.of(pageNum - 1, pageSize, sort));

        List<PaymentEntity> allPosts = page.getContent();

        // Filter posts by month
        List<PaymentEntity> filteredPosts = allPosts.stream()
                .filter(post -> {
                    // Convert Date to LocalDate
                    LocalDate regDate = post.getReg().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    // Compare month values
                    return regDate.getMonthValue() == month;
                })
                .collect(Collectors.toList());

        int filteredCount = filteredPosts.size();

        model.addAttribute("posts", filteredPosts);
        model.addAttribute("count", filteredCount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        int pageCount = filteredCount / pageSize + (filteredCount % pageSize == 0 ? 0 : 1);
        int startPage = (pageNum / 10) * 10 + 1;
        int pageBlock = 10;
        int endPage = startPage + pageBlock - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("endPage", endPage);
        model.addAttribute("month", month); // 추가: 현재 월을 모델에 추가
    }

}
