package com.project.pickyou.entity;

import com.project.pickyou.dto.EquipmentDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "equipment")
@IdClass(EquipmentID.class)
public class EquipmentEntity {
    @Id
    @Column(name = "member_id")
    private String memberId;

    @Id
    private String name;

    @Builder
    public EquipmentEntity(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public EquipmentDTO toEquipmentDTO(){
        return EquipmentDTO.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }
}
