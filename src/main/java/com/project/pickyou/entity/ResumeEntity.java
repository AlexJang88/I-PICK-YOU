package com.project.pickyou.entity;

import com.project.pickyou.dto.ResumeDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;


import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Entity
@Data
@DynamicInsert
@Table(name = "resume")
public class ResumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id")
    private String memberId;
    private int wage;
    private String local;
    private String introduction;
    @Column(name = "reg_type")
    private int regType;
    private Date reg;

    @OneToOne
    @JoinColumn(name = "member_id",referencedColumnName = "id",insertable = false,updatable = false)
    private MemberEntity member;

    @OneToOne(mappedBy = "resume")
    private CareerEntity career;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 수정
    private List<JobEntity> job;

    @Builder
    public ResumeEntity(Long id, String memberId, int wage, String local,
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

    public ResumeDTO toResumeDTO() {
        return ResumeDTO.builder()
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
