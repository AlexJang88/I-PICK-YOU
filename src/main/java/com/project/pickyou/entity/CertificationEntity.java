package com.project.pickyou.entity;

import com.project.pickyou.dto.CertificationDTO;
import com.project.pickyou.dto.EquipmentDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "certification")
@IdClass(CertificationID.class)
public class CertificationEntity {
    @Id
    @Column(name = "resume_id")
    private Long resumeId;

    @Id
    private String name;

    @Builder
    public CertificationEntity(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public CertificationDTO toCertificationDTO(){
        return CertificationDTO.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }
}
