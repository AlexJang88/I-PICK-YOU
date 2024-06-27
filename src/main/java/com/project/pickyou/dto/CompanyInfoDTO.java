package com.project.pickyou.dto;

import com.project.pickyou.entity.CompanyInfoEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyInfoDTO {
    private String id;
    private String name;
    private String corpno;
    private String job;
    private String companyName;


    @Builder
    public CompanyInfoDTO(String id, String name, String corpno, String job, String companyName){
        super();
        this.id=id;
        this.name=name;
        this.corpno=corpno;
        this.job=job;
        this.companyName=companyName;
    }
    public CompanyInfoEntity toCompanyInfoEntity(){
        return CompanyInfoEntity.builder()
                .id(this.id)
                .name(this.name)
                .corpno(this.corpno)
                .job(this.job)
                .companyName(this.companyName)
                .build();
    }



}
