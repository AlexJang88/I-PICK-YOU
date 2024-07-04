package com.project.pickyou.entity;

import com.project.pickyou.dto.TeamResumeDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "team_resume")
@DynamicUpdate
@DynamicInsert
public class TeamResumeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id")
    private String memberId;
    private String job;
    @Column(name = "team_name")
    private String teamName;
    private String address;
    private int number;
    private String phone;
    private String introduction;
    private String advantage;
    private String profile;
    private int status;
    @UpdateTimestamp
    private Date reg;

    @OneToOne
    @JoinColumn(name = "member_id",referencedColumnName = "id",insertable = false,updatable = false)
    private MemberEntity member;

    @Builder
    public TeamResumeEntity(Long id, String memberId, String job, String teamName, String address, int number, String phone, String introduction, String advantage, String profile, int status, Date reg) {
        this.id = id;
        this.memberId = memberId;
        this.job = job;
        this.teamName = teamName;
        this.address = address;
        this.number = number;
        this.phone = phone;
        this.introduction = introduction;
        this.advantage = advantage;
        this.profile = profile;
        this.status = status;
        this.reg = reg;
    }

    public TeamResumeDTO toTeam_ResumeDTO() {
        return TeamResumeDTO.builder()
                .id(this.id)
                .memberId(this.memberId)
                .job(this.job)
                .teamName(this.teamName)
                .address(this.address)
                .number(this.number)
                .phone(this.phone)
                .introduction(this.introduction)
                .advantage(this.advantage)
                .profile(this.profile)
                .status(this.status)
                .reg(this.reg)
                .build();
    }
}