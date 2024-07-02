package com.project.pickyou.dto;


import com.project.pickyou.entity.TrainningEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class TrainningDTO {
    private Long id;
    private String companyId;
    private String title;
    private String position;
    private String trainner;
    private String etc;
    private String name;
    private String address;
    private String contact;
    private int readCount;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date reg;

    @Builder
    public TrainningDTO(Long id, String companyId, String title, String position, String trainner, String etc, String name, String address, String contact, int readCount, String content, Date reg) {
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
        this.content = content;
        this.reg = reg;
    }

    public TrainningEntity toTrainningEntity() {
        return TrainningEntity.builder()
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
                .content(this.content)
                .reg(this.reg)
                .build();
    }
}
