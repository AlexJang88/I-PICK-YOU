package com.project.pickyou.dto;

import com.project.pickyou.entity.RecruitStateEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class RecruitStateDTO {

    private Long id;
    private String memberId;
    private Long recruitId;
    private Date reg;

    @Builder
    public RecruitStateDTO(Long id, String memberId, Long recruitId, Date reg ) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.recruitId = recruitId;
        this.reg = reg;

    }


    public RecruitStateEntity toRecruitStateEntity() {
        return RecruitStateEntity.builder()

                .id(this.id)
                .memberId(this.memberId)
                .recruitId(this.recruitId)
                .reg(this.reg)
                 .build();
    }










}

