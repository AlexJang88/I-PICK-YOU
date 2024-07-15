package com.project.pickyou.service;

import com.project.pickyou.dto.AlarmDTO;
import com.project.pickyou.entity.AlarmEntity;
import com.project.pickyou.entity.MemberEntity;
import com.project.pickyou.repository.AlarmJPARepository;
import com.project.pickyou.repository.MemberInfoJAPRepository;
import com.project.pickyou.repository.MemberJPARepository;
import jakarta.transaction.Transactional;
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
public class AlarmServiceImpl implements AlarmService{

    private final AlarmJPARepository alarmJPARepository;

    private final MemberJPARepository memberJPARepository;

    @Override  //쪽지 받은 목록
    public void findList(Model model, String id, int pageNum) {
        int pageSize = 10;
        Long longCount = alarmJPARepository.countByReaderId(id); // 쪽지 수량 체크
        int count = longCount.intValue();  //롱으로 받은 카운트를 인트로 변환
        Sort sort = Sort.by(Sort.Order.desc("reg"));  //내림차순
        Page<AlarmEntity> page = alarmJPARepository.findByReaderId(id, PageRequest.of(pageNum - 1, pageSize, sort));  //아이디에 맞는 리스트 가져오기


        List<AlarmEntity> alarmEntityList = page.getContent();  //리스트로 담기

        model.addAttribute("alarmEntityList", alarmEntityList);
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

    @Override //쪽지 상세내용
    @Transactional
    public void findContent(Model model, String id,Long num) {

        int status = 2;

        AlarmEntity find = alarmJPARepository.findByIdAndReaderId(num,id); //내용찾고
        if(find.getStatus() == 1){
            find.setStatus(status); //읽음으로 세팅
        }
        alarmJPARepository.save(find);
        AlarmEntity alarm = alarmJPARepository.findByIdAndReaderId(num,id); //수정된 내용으로 꺼냄
        model.addAttribute("alarm", alarm);
    }


    //관리자가 보낸 쪽지내용
    // 리스트
    @Override
    public void adminSpend(Model model, String id, int pageNum) {
        int pageSize = 10;
        Long longCount = alarmJPARepository.countByMemberId(id); // 쪽관리자가 보낸 쪽 수량 체크
        int count = longCount.intValue();  //롱으로 받은 카운트를 인트로 변환
        Sort sort = Sort.by(Sort.Order.desc("reg"));  //내림차순

        Page<AlarmEntity> page = alarmJPARepository.findByMemberId(id, PageRequest.of(pageNum - 1, pageSize, sort));  //아이디에 맞는 리스트 가져오기

        List<AlarmEntity> alarmSpendList = page.getContent();  //리스트로 담기

        model.addAttribute("alarmSpendList", alarmSpendList);
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

    @Override  //관리자가 보낸 쪽지내용
    public void adminSpendContent(Model model, String id, Long num) {

        AlarmEntity alarmContent = alarmJPARepository.findByIdAndMemberId(num,id); //수정된 내용으로 꺼냄
        model.addAttribute("alarm", alarmContent);
    }

    @Override  //특정유저에게 쪽지보내기
    public void insertOneUser(String id, String name, String message) {

        int status = 1;

        AlarmDTO alarmDTO = new AlarmDTO();

        alarmDTO.setMemberId(id);
        alarmDTO.setReaderId(name);
        alarmDTO.setContent(message);
        alarmDTO.setStatus(status);

        alarmJPARepository.save(alarmDTO.toAlarmEntity());  //특정회원 보내기
    }

    @Override  //여러 유저한테 보내기
    public void insertManyUser(String id, String name, String message, String type) {
        int status = 1;
        String business = "ROLE_COMPANY";
        String users = "ROLE_USER";

        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setMemberId(id);
        alarmDTO.setContent(message);
        alarmDTO.setStatus(status);

        if(type.equals("1")){
            List<MemberEntity> allUsers = memberJPARepository.findAll(); // 모든 회원 가져오기
            for (MemberEntity alluser : allUsers) {
                alarmDTO.setReaderId(alluser.getId()); // 사용자 ID를 memberId로 설정
                alarmJPARepository.save(alarmDTO.toAlarmEntity());
            }
        }else if(type.equals("2")){
            List<MemberEntity> businessUsers  = memberJPARepository.findByAuth(business); // 모든 사업자 가져오기
            for (MemberEntity companyuser : businessUsers) {
                alarmDTO.setReaderId(companyuser.getId()); // 사용자 ID를 memberId로 설정
                alarmJPARepository.save(alarmDTO.toAlarmEntity());
            }
        }else{
            List<MemberEntity> generaluser  = memberJPARepository.findByAuth(users); // 모든 일반 회원가져오기
            for (MemberEntity user : generaluser) {
                alarmDTO.setReaderId(user.getId()); // 사용자 ID를 memberId로 설정
                alarmJPARepository.save(alarmDTO.toAlarmEntity());
            }
        }

    }


}
