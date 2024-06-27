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

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "confirm")
public class ConfirmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private String memberId;
    private String contract;
    @Column(name = "company_id")
    private String companyId;
    private int apply;
    private Date start;
    private Date end;

    @Builder
    public ConfirmEntity(Long id, String memberId, String contract, String companyId, Integer apply, Date start, Date end) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.contract = contract;
        this.companyId = companyId;
        this.apply = apply;
        this.start = start;
        this.end = end;
    }

    public ConfirmDTO toConfirmDTO() {
        return ConfirmDTO.builder()
                .id(this.id)
                .memberId(this.memberId)
                .contract(this.contract)
                .companyId(this.companyId)
                .apply(this.apply)
                .start(this.start)
                .end(this.end)
                .build();
    }
}
