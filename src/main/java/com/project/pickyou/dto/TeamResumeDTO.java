package com.project.pickyou.dto;

import com.project.pickyou.entity.TeamResumeEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TeamResumeDTO {
    private Long id;
    private String memberId;
    private String job;
    private String teamName;
    private String address;
    private int number;
    private String phone;
    private String introduction;
    private String advantage;
    private String profile;
    private int status;
    private Date reg;

    @Builder
    public TeamResumeDTO(Long id,String memberId,String job, String teamName, String address, int number, String phone, String introduction, String advantage, String profile, int status, Date reg) {
        this.id = id;
        this.memberId=memberId;
        this.job=job;
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

    public TeamResumeEntity toTeam_ResumeEntity() {
        return TeamResumeEntity.builder()
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