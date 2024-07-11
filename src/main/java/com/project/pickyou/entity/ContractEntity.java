package com.project.pickyou.entity;

import com.project.pickyou.dto.ContractDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "contract")
@DynamicInsert
@DynamicUpdate
public class ContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company_id")
    private String companyId;
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate; // 기본값: 오늘 날짜로부터 1년 후
    private String location;
    private String job;
    @Column(name = "work_start_time")
    private String workStartTime;
    @Column(name = "work_end_time")
    private String workEndTime;
    @Column(name = "works_schedule")
    private int worksSchedule;
    @Column(name = "wage_type")
    private String wageType; // 기본값: 일급
    private int wage;
    private String etc; // 기본값: 없음
    @Column(name = "pay_date")
    private String payDate; // 기본값: 매월
    @Column(name = "custom_pay_date")
    private int customPayDate; // 기본값: 0 (무시)
    @Column(name = "wage_into")
    private String wageInto; // 기본값: 근로자에게 직접지급
    private String insurance1;
    private String insurance2;
    private String insurance3;
    private String insurance4;// 기본값: 빈 배열
    @Column(name = "contract_date")
    private LocalDate contractDate;// 기본값: 오늘 날짜


    @Builder
    public ContractEntity(Long id,
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
                          LocalDate contractDate) {
        this.id = id;
        this.companyId = companyId;
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.job = job;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.worksSchedule = worksSchedule;
        this.wageType = wageType;
        this.wage = wage;
        this.etc = etc;
        this.payDate = payDate;
        this.customPayDate = customPayDate;
        this.wageInto = wageInto;
        this.insurance1 = insurance1;
        this.insurance2 = insurance2;
        this.insurance3 = insurance3;
        this.insurance4 = insurance4;
        this.contractDate = contractDate;
    }

    public ContractDTO toContractDTO() {
        return ContractDTO.builder()
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
