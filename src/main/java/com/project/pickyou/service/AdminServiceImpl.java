package com.project.pickyou.service;

import com.project.pickyou.dto.PointDTO;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.entity.PaymentEntity;
import com.project.pickyou.entity.PointEntity;
import com.project.pickyou.entity.TrainningEntity;
import com.project.pickyou.repository.MemberInfoJAPRepository;
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
import java.time.Year;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final PointJPARepository pointJPA;
    private final MemberJPARepository memberJPARepository;
    private final MemberInfoJAPRepository memberInfoJAPRepository;
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

    @Override  //유저삭제
    public void userDelete(String memberId) { //일반, 사업자 유저 삭제

        memberJPARepository.deleteById(memberId);
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

    // 테스트 @@ ///////////////////////////
    @Override
    public List<Integer> generateYearList() {
        List<Integer> years = new ArrayList<>();
        int currentYear = Year.now().getValue();

        for (int year = 2020; year <= currentYear; year++) {
            years.add(year);
        }

        return years;
    }

    @Override
    public void AllPostsGIVE(Model model, int status, int pageNum, int year, int month) {
        int pageSize = 10;
        int count = pointJPA.countByStatus(status);

        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<PointEntity> page = pointJPA.findByStatus(status, PageRequest.of(pageNum - 1, pageSize, sort));

        List<PointEntity> allPosts = page.getContent();

        // Filter posts by year and month
        List<PointEntity> filteredPosts = allPosts.stream()
                .filter(post -> {
                    // Convert Date to LocalDate
                    LocalDate regDate = post.getReg().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    // Compare year and month
                    return regDate.getYear() == year && regDate.getMonthValue() == month;
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
        model.addAttribute("year", year); // 추가: 현재 연도를 모델에 추가
    }
    // 테스트

    /*// 포인트 지급, 사용내역
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
    }*/


    // 결제, 포인트 사용내역 가져오기
    @Override
    public void AllpaymentANDpoint(Model model, int pointHistory, int pageNum, int year, int month) {
        int pageSize = 10;
        int count = paymentJPA.countByPointHistory(pointHistory);

        Sort sort = Sort.by(Sort.Order.desc("reg"));
        Page<PaymentEntity> page = paymentJPA.findByPointHistory(pointHistory, PageRequest.of(pageNum - 1, pageSize, sort));

        List<PaymentEntity> allPosts = page.getContent();

        // Filter posts by year and month
        List<PaymentEntity> filteredPosts = allPosts.stream()
                .filter(post -> {
                    // Convert Date to LocalDate
                    LocalDate regDate = post.getReg().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    // Compare year and month
                    return regDate.getYear() == year && regDate.getMonthValue() == month;
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
        model.addAttribute("year", year); // 추가: 현재 연도를 모델에 추가
    }






    // 결제, 포인트 사용내역 가져오기
    public void salesInfo(Model model, int pointHistory, int year, int month, String chartType) {
        // 페이지 크기 설정
        int pageSize = Integer.MAX_VALUE; // 페이지 크기를 매우 큰 값으로 설정
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Order.desc("reg")));

        // 데이터 조회
        Page<PaymentEntity> page = paymentJPA.findByPointHistory(pointHistory, pageable);

        // 페이지 내용 가져오기
        List<PaymentEntity> allPosts = page.getContent();

        // 연도와 월로 필터링
        List<PaymentEntity> filteredPosts = allPosts.stream()
                .filter(post -> {
                    // 날짜를 LocalDate로 변환
                    LocalDate regDate = post.getReg().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return regDate.getYear() == year && regDate.getMonthValue() == month;
                })
                .collect(Collectors.toList());

        // 필터링된 데이터 집계 (money와 point)
        Map<LocalDate, Integer> moneyAggregatedData = filteredPosts.stream()
                .collect(Collectors.groupingBy(
                        post -> post.getReg().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.summingInt(PaymentEntity::getMoney)
                ));

        Map<LocalDate, Integer> pointAggregatedData = filteredPosts.stream()
                .collect(Collectors.groupingBy(
                        post -> post.getReg().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.summingInt(PaymentEntity::getPoint)
                ));

        // 총합 계산
        int totalMoney = moneyAggregatedData.values().stream().mapToInt(Integer::intValue).sum();
        int totalPoint = pointAggregatedData.values().stream().mapToInt(Integer::intValue).sum();
        int totalDifference = totalMoney - totalPoint;

        // 두 데이터 집계 결과를 하나로 합쳐서 차이 계산
        Map<LocalDate, Integer> combinedData = new TreeMap<>(moneyAggregatedData); // 날짜 정렬을 위해 TreeMap 사용

        pointAggregatedData.forEach((date, pointValue) ->
                combinedData.put(date, combinedData.getOrDefault(date, 0) - pointValue));

        // 차트 데이터 형식으로 변환
        List<Map<String, Object>> chartDataMoney = moneyAggregatedData.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("x", entry.getKey().toString()); // 날짜를 문자열로 변환
                    dataPoint.put("y", entry.getValue()); // 금액
                    return dataPoint;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> chartDataPoint = pointAggregatedData.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("x", entry.getKey().toString()); // 날짜를 문자열로 변환
                    dataPoint.put("y", entry.getValue()); // 포인트
                    return dataPoint;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> chartDataDifference = combinedData.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("x", entry.getKey().toString()); // 날짜를 문자열로 변환
                    dataPoint.put("y", entry.getValue()); // 차이 (money - point)
                    return dataPoint;
                })
                .collect(Collectors.toList());

        // 모델에 데이터 추가
        model.addAttribute("posts", filteredPosts);
        model.addAttribute("chartDataMoney", chartDataMoney);
        model.addAttribute("chartDataPoint", chartDataPoint);
        model.addAttribute("chartDataDifference", chartDataDifference);
        model.addAttribute("chartType", chartType); // 현재 그래프 타입을 모델에 추가
        model.addAttribute("month", month); // 현재 월을 모델에 추가
        model.addAttribute("year", year); // 현재 연도를 모델에 추가
        model.addAttribute("totalMoney", totalMoney); // 총 금액
        model.addAttribute("totalPoint", totalPoint); // 총 포인트
        model.addAttribute("totalDifference", totalDifference); // 총 차이
    }






    @Override
    public void getnPoitApproval(Model model, int pageNum) {  //유저 포인트 승인하는곳에 리스트 가져오기
        int pageSize = 10;
        int status = 3;  //1적립, 2 차감, 3 포인트 지급요청
        Long longCount = (long) pointJPA.countByStatus(status);// //포인트테이블 3인사람만(변환요청한사람)

        int count =longCount.intValue(); //타입변환
        Sort sort = Sort.by(Sort.Order.desc("reg")); //내림차순

        Page<PointEntity> page = pointJPA.findByStatus(status, PageRequest.of(pageNum - 1, pageSize, sort));

        List<PointEntity> getpointlist = page.getContent();

        model.addAttribute("getpointlist", getpointlist);
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
    @Transactional
    public void patchPoint(PointDTO pointDTO) {// 포인트 변환
        Optional<PointEntity> pointEntity = pointJPA.findById(pointDTO.getId());
        int status = 2;

        if(pointEntity.isPresent()){
            PointEntity point = pointEntity.get(); // 포인트 정보를 여기에 담고
            point.setStatus(status);  //포인트 상태를 2(차감)으로 바꾸고
            pointJPA.save(point);  //다시 저장
        }

    }


}
