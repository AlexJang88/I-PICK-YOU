package com.project.pickyou.entity;

import com.project.pickyou.dto.EducationDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "education")
public class EducationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "company_id")
    private String companyId;
    private int money;
    private String contact;
    private String preparation;
    private String address;
    private String content;
    @Column(name = "read_count")
    private int readCount;
    private Date reg;

    @Builder
    public EducationEntity(Long id, String title, String companyId, int money, String contact, String preparation, String address, String content, int readCount, Date reg) {
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

    public EducationDTO toEducationDTO() {
        return EducationDTO.builder()
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
