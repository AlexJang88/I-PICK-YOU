package com.project.pickyou.entity;

import com.project.pickyou.dto.RecruitDetailDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "recruit_detail")
public class RecruitDetailEntity {

    @Id
    @Column(name = "recruit_id")
    private Long recruitId;
    private int wage;
    private String content;
    private int age;
    private int gender;
    private int worker;
    private String manager;
    private String contact;
    private String qualification;
    private String detail;

    @Builder
    public RecruitDetailEntity(Long recruitId, int wage, String content, int age, int gender, int worker,
                            String manager,String contact, String qualification, String detail) {
        super();
        this.recruitId = recruitId;
        this.wage = wage;
        this.content = content;
        this.age = age;
        this.gender = gender;
        this.worker = worker;
        this.manager = manager;
        this.contact = contact;
        this.qualification = qualification;
        this.detail = detail;
    }


    public RecruitDetailDTO toRecruitDetailDTO(){
        return RecruitDetailDTO.builder()
                .recruitId(this.recruitId)
                .wage(this.wage)
                .content(this.content)
                .age(this.age)
                .gender(this.gender)
                .worker(this.worker)
                .manager(this.manager)
                .contact(this.contact)
                .worker(this.worker)
                .qualification(this.qualification)
                .detail(this.detail)
                .build();
    }
}
