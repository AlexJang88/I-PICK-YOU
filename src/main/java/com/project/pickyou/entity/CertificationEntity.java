package com.project.pickyou.entity;

import com.project.pickyou.dto.CertificationDTO;
import com.project.pickyou.dto.EquipmentDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "certification")
@IdClass(CertificationID.class)
public class CertificationEntity {
    @Id
    @Column(name = "member_id")
    private String memberId;

    @Id
    private String name;

    @Builder
    public CertificationEntity(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public CertificationDTO toCertificationDTO(){
        return CertificationDTO.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }
}
