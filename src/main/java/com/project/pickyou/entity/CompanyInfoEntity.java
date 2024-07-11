package com.project.pickyou.entity;

import com.project.pickyou.dto.CompanyInfoDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "company_info")
public class CompanyInfoEntity {
    @Id
    private String id;
    private String name;
    private String corpno;
    private String job;
    @Column(name = "company_name")
    private String companyName;


    @Builder
    public CompanyInfoEntity(String id, String name, String corpno, String job, String companyName){
        super();
        this.id=id;
        this.name=name;
        this.corpno=corpno;
        this.job=job;
        this.companyName=companyName;
    }
    public CompanyInfoDTO toCompanyInfoDTO(){
        return CompanyInfoDTO.builder()
                .id(this.id)
                .name(this.name)
                .corpno(this.corpno)
                .job(this.job)
                .companyName(this.companyName)
                .build();
    }
}
