package com.project.pickyou.entity;

import com.project.pickyou.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class MemberEntity {
    @Id
    private String id;
    private String pw;
    private String address;
    private String phone;
    private String email;
    private String profile;
    private String auth;
    private Date reg;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id",insertable = false,updatable = false)
    private MemberInfoEntity memberInfo;

    @OneToOne
    @JoinColumn(name="id",referencedColumnName = "id",insertable = false,updatable = false)
    private CompanyInfoEntity companyInfo;

    @Builder
    public MemberEntity(String id, String pw, String address, String phone, String email, String profile, String auth, Date reg){
        super();
        this.id=id;
        this.pw=pw;
        this.address=address;
        this.phone=phone;
        this.email=email;
        this.profile=profile;
        this.auth=auth;
        this.reg=reg;
    }

    public MemberDTO toMemberDTO(){
        return MemberDTO.builder()
                .id(this.id)
                .pw(this.pw)
                .address(this.address)
                .phone(this.phone)
                .email(this.email)
                .profile(this.profile)
                .auth(this.auth)
                .reg(this.reg)
                .build();
    }
}
