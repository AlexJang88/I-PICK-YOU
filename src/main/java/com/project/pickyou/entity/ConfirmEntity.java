package com.project.pickyou.entity;

import com.project.pickyou.dto.ConfirmDTO;
import jakarta.persistence.*;
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
    @Column(name="recruit_id")
    private Long recruitId;
    private Date reg;

    @OneToOne
    @JoinColumn(name = "recruit_id",referencedColumnName = "id",updatable = false,insertable = false)
    private RecruitEntity recruit;

    @Builder
    public ConfirmEntity(Long id, String memberId, Long contractId, String companyId, Integer apply,Long recruitId,Date reg) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.contractId = contractId;
        this.companyId = companyId;
        this.apply = apply;
        this.recruitId = recruitId;
        this.reg=reg;

    }

    public ConfirmDTO toConfirmDTO() {
        return ConfirmDTO.builder()
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
