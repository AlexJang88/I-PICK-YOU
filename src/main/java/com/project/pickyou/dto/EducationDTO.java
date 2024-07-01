package com.project.pickyou.dto;

import com.project.pickyou.entity.EducationEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class EducationDTO {
    private Long id;
    private String title;
    private String companyId;
    private int money;
    private String contact;
    private String preparation;
    private String address;
    private String content;
    private int readCount;
    private Date reg;

    @Builder
    public EducationDTO(Long id, String title, String companyId, int money, String contact, String preparation, String address, String content, int readCount, Date reg) {
        this.id = id;
        this.title = title;
        this.companyId = companyId;
        this.money = money;
        this.contact = contact;
        this.preparation = preparation;
        this.address = address;
        this.content = content;
        this.readCount = readCount;
        this.reg = reg;
    }

    public EducationEntity toEducationEntity() {
        return EducationEntity.builder()
                .id(this.id)
                .title(this.title)
                .companyId(this.companyId)
                .money(this.money)
                .contact(this.contact)
                .preparation(this.preparation)
                .address(this.address)
                .content(this.content)
                .readCount(this.readCount)
                .reg(this.reg)
                .build();
    }
}
