package com.project.pickyou.dto;

import com.project.pickyou.entity.MemberEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MemberDTO {
    private String id;
    private String pw;
    private String address;
    private String phone;
    private String email;
    private String profile;
    private String auth;
    private Date reg;


    @Builder
    public MemberDTO(String id, String pw, String address, String phone, String email, String profile, String auth, Date reg){
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
    public MemberEntity toMemberEntity(){
        return MemberEntity.builder()
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
