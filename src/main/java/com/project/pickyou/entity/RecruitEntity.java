package com.project.pickyou.entity;

import com.project.pickyou.dto.RecruitDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "recruit")
public class RecruitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private int status;
    private int readCount;
    @Column(name="member_id")
    private String memberId;
    private Date reg;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;

    @Builder
    public RecruitEntity(Long id, String title, String content, int status, int readCount,String memberId, Date reg,
                      Date startDate, Date endDate) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.readCount = readCount;
        this.memberId = memberId;
        this.reg = reg;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public RecruitDTO toRecruitDTO() {
        return RecruitDTO.builder()

                .id(this.id)
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .readCount(this.readCount)
                .memberId(this.memberId)
                .reg(this.reg)
                .startDate(this.startDate)
                .endDate(this.endDate)
                 .build();
    }
}
