package com.project.pickyou.entity;

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
@Table(name = "equipment")
@IdClass(EquipmentID.class)
public class EquipmentEntity {
    @Id
    @Column(name = "resume_id")
    private Long resumeId;

    @Id
    private String name;

    @Builder
    public EquipmentEntity(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public EquipmentDTO toEquipmentDTO(){
        return EquipmentDTO.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }
}
