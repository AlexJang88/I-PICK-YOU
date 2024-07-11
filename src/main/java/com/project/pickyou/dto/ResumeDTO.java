package com.project.pickyou.dto;

import com.project.pickyou.entity.ResumeEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class ResumeDTO {
    private Long id;
    private String memberId;
    private int wage;
    private String local;
    private String introduction;
    private int regType;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private Date reg;

    @Builder
    public ResumeDTO(Long id, String memberId, int wage, String local,
                     String introduction, int regType, Date reg) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.wage = wage;
        this.local = local;
        this.introduction = introduction;
        this.regType = regType;
        this.reg = reg;
    }

    public ResumeEntity toResumeEntity() {
        return ResumeEntity.builder()
                .id(this.id)
                .memberId(this.memberId)
                .wage(this.wage)
                .local(this.local)
                .introduction(this.introduction)
                .regType(this.regType)
                .reg(this.reg)
                .build();
    }
}
