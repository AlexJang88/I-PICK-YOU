package com.project.pickyou.dto;

import com.project.pickyou.entity.ConfirmEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class ConfirmDTO {
    private Long id;
    private String memberId;
    private Long contractId;
    private String companyId;
    private int apply;
    private Long recruitId;
    private Date reg;

    @Builder
    public ConfirmDTO(Long id, String memberId, Long contractId, String companyId, Integer apply,Long recruitId,Date reg ) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.contractId = contractId;
        this.companyId = companyId;
        this.apply = apply;
        this.recruitId=recruitId;
        this.reg=reg;
    }

    public ConfirmEntity toConfirmEntity() {
        return ConfirmEntity.builder()
                .id(this.id)
                .memberId(this.memberId)
                .contractId(this.contractId)
                .companyId(this.companyId)
                .apply(this.apply)
                .recruitId(this.recruitId)
                .reg(this.reg)
                .build();
    }
}
