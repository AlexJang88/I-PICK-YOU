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
    private Date start;
    private Date end;

    @Builder
    public ConfirmDTO(Long id, String memberId, Long contractId, String companyId, Integer apply, Date start, Date end) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.contractId = contractId;
        this.companyId = companyId;
        this.apply = apply;
        this.start = start;
        this.end = end;
    }

    public ConfirmEntity toConfirmEntity() {
        return ConfirmEntity.builder()
                .id(this.id)
                .memberId(this.memberId)
                .contractId(this.contractId)
                .companyId(this.companyId)
                .apply(this.apply)
                .start(this.start)
                .end(this.end)
                .build();
    }
}
