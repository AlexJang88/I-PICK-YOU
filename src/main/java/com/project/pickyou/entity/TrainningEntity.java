package com.project.pickyou.entity;

import com.project.pickyou.dto.TrainningDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "trainning")
public class TrainningEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company_id")
    private String companyId;
    private String title;
    private String position;
    private String trainner;
    private String etc;
    private String name;
    private String address;
    private String contact;
    @Column(name = "read_count")
    private int readCount;
    @Column(name = "image_registration_id")
    private Long imageRegistrationId;
    private String content;
    private Date reg;

    @Builder
    public TrainningEntity(Long id, String companyId, String title, String position, String trainner, String etc, String name, String address, String contact, int readCount, Long imageRegistrationId, String content, Date reg) {
        this.id = id;
        this.companyId = companyId;
        this.title = title;
        this.position = position;
        this.trainner = trainner;
        this.etc = etc;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.readCount = readCount;
        this.imageRegistrationId = imageRegistrationId;
        this.content = content;
        this.reg = reg;
    }

    public TrainningDTO toTrainingDTO() {
        return TrainningDTO.builder()
                .id(this.id)
                .companyId(this.companyId)
                .title(this.title)
                .position(this.position)
                .trainner(this.trainner)
                .etc(this.etc)
                .name(this.name)
                .address(this.address)
                .contact(this.contact)
                .readCount(this.readCount)
                .imageRegistrationId(this.imageRegistrationId)
                .content(this.content)
                .reg(this.reg)
                .build();
    }
}
