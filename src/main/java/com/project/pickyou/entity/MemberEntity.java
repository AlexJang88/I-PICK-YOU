package com.project.pickyou.entity;

import com.project.pickyou.dto.MemberDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert   //인서트시에 사용
@DynamicUpdate    // 업데이트시에 사용
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
    @CreationTimestamp
    private Date reg;


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
