package com.project.pickyou.dto;


import com.project.pickyou.entity.AgencyEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AgencyDTO {

    private Long id;
    private String companyId;
    private String companyName;
    private String business;
    private String contact;
    private String address;
    private String content;
    private int readCount;
    private Date reg;

    @Builder
    public AgencyDTO(Long id, String companyId, String companyName, String business,
                     String contact, String address, String content, int readCount, Date reg){
        this.id = id;
        this.companyId = companyId;
        this.companyName =companyName;
        this.contact = contact;
        this.business = business;
        this.address = address;
        this.content = content;
        this.readCount = readCount;
        this.reg = reg;

    }

    public AgencyEntity toAgencyEntity(){
        return AgencyEntity.builder()
                .id(this.id)
                .companyId(this.companyId)
                .companyName(this.companyName)
                .contact(this.contact)
                .business(this.business)
                .address(this.address)
                .content(this.content)
                .readCount(this.readCount)
                .reg(this.reg)
                .build();
    }

}
