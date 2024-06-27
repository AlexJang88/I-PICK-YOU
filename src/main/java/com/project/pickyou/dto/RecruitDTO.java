package com.project.pickyou.dto;

import com.project.pickyou.entity.RecruitEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class RecruitDTO {

    private Long id;
    private String title;
    private String content;
    private int status;
    private int readCount;
    private String memberId;
    private Date reg;
    private Date startDate;
    private Date endDate;

    @Builder
    public RecruitDTO(Long id, String title, String content, int status, int readCount,String memberId, Date reg,
                      Date startDate, Date endDate ) {
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



    // Convert DTO to Entity
    public RecruitEntity toRecruitEntity() {
        return RecruitEntity.builder()
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
