package com.project.pickyou.dto;

import com.project.pickyou.entity.ContractEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
public class ContractDTO {
    private Long id;
    private String companyId;
    private String memberId;
    private LocalDate startDate;
    private LocalDate endDate ;
    private String location;
    private String job;
    private String workStartTime;
    private String workEndTime;
    private int worksSchedule;
    private String wageType ;
    private int wage;
    private String etc ;
    private String payDate ;
    private int customPayDate ;
    private String wageInto ;
    private String insurance1;
    private String insurance2;
    private String insurance3;
    private String insurance4;
    private LocalDate contractDate;



    @Builder
    public ContractDTO( Long id,
            String companyId,
            String memberId,
            LocalDate startDate,
             LocalDate endDate,
             String location,
             String job,
             String workStartTime,
             String workEndTime,
             int worksSchedule,
             String wageType,
             int wage,
             String etc,
             String payDate,
             int customPayDate,
             String wageInto,
             String insurance1,
             String insurance2,
             String insurance3,
             String insurance4,
             LocalDate contractDate)
             {
        this.id=id;
        this.companyId=companyId;
        this.memberId=memberId;
        this.startDate=startDate;
        this.endDate=endDate;
        this.location=location;
        this.job=job;
        this.workStartTime=workStartTime;
        this.workEndTime=workEndTime;
        this.worksSchedule=worksSchedule;
        this.wageType=wageType;
        this.wage=wage;
        this.etc=etc;
        this.payDate=payDate;
        this.customPayDate=customPayDate;
        this.wageInto=wageInto;
        this.insurance1=insurance1;
        this.insurance2=insurance2;
        this.insurance3=insurance3;
        this.insurance4=insurance4;
        this.contractDate=contractDate;
    }
    public ContractEntity toContractEntity() {
        return ContractEntity.builder()
                .id(this.id)
                .companyId(this.companyId)
                .memberId(this.memberId)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .location(this.location)
                .job(this.job)
                .workStartTime(this.workStartTime)
                .workEndTime(this.workEndTime)
                .worksSchedule(this.worksSchedule)
                .wageType(this.wageType)
                .wage(this.wage)
                .etc(this.etc)
                .payDate(this.payDate)
                .customPayDate(this.customPayDate)
                .wageInto(this.wageInto)
                .insurance1(this.insurance1)
                .insurance2(this.insurance2)
                .insurance3(this.insurance3)
                .insurance4(this.insurance4)
                .contractDate(this.contractDate)
                .build();
    }




}
