package com.project.pickyou.dto;

import com.project.pickyou.entity.RecruitDetailEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecruitDetailDTO {

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
    public RecruitDetailDTO(Long recruitId, int wage, String content, int age, int gender, int worker,
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


    public RecruitDetailEntity toRecruitDetailEntity(){
        return RecruitDetailEntity.builder()
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
