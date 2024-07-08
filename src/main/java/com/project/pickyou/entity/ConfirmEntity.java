package com.project.pickyou.entity;

import com.project.pickyou.dto.ConfirmDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "confirm")
@DynamicInsert
@DynamicUpdate
public class ConfirmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id")
    private String memberId;
    @Column(name="contract_id")
    private Long contractId;
    @Column(name = "company_id")
    private String companyId;
    private int apply;
    private Date start;
    private Date end;

    @Builder
    public ConfirmEntity(Long id, String memberId, Long contractId, String companyId, Integer apply, Date start, Date end) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.contractId = contractId;
        this.companyId = companyId;
        this.apply = apply;
        this.start = start;
        this.end = end;
    }

    public ConfirmDTO toConfirmDTO() {
        return ConfirmDTO.builder()
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
