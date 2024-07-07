package com.project.pickyou.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDTO {
    private LocalDate startdate;
    private LocalDate enddate ; // 기본값: 오늘 날짜로부터 1년 후
    private String location;
    private String job;
    private String workStartTime;
    private String workEndTime;
    private int worksShedule;
    private String wageType ; // 기본값: 일급
    private int wage;
    private String etc ; // 기본값: 없음
    private String payDate ; // 기본값: 매월
    private int customPayDate ; // 기본값: 0 (무시)
    private String wageInto ; // 기본값: 근로자에게 직접지급
    private String[] insurance ; // 기본값: 빈 배열
    private LocalDate contractDate;// 기본값: 오늘 날짜
    private String companyCEO;
    private String companyAddress;
    private String companyName;
    private String companyContact;
    private String memberAddress;
    private String memberContact;
    private String memberName;




}
