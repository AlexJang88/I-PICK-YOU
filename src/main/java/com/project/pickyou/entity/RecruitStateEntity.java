package com.project.pickyou.entity;

import com.project.pickyou.dto.RecruitStateDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
@Data
@NoArgsConstructor
@Entity
@Table(name = "recruit_state")
@DynamicInsert
@DynamicUpdate
public class RecruitStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="member_id")
    private String memberId;
    @Column(name="recruit_id")
    private Long recruitId;
    private Date reg;

    @OneToOne
    @JoinColumn(name="recruit_id",referencedColumnName = "id",insertable = false,updatable = false)
    private RecruitEntity recruit;

    @Builder
    public RecruitStateEntity(Long id, String memberId, Long recruitId, Date reg ) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.recruitId = recruitId;
        this.reg = reg;

    }


    public RecruitStateDTO toRecruitStateDTO() {
        return RecruitStateDTO.builder()

                .id(this.id)
                .memberId(this.memberId)
                .recruitId(this.recruitId)
                .reg(this.reg)
                .build();
    }






}
