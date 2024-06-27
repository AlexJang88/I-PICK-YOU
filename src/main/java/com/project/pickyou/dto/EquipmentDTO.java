package com.project.pickyou.dto;

import com.project.pickyou.entity.EquipmentEntity;
import com.project.pickyou.entity.JobEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipmentDTO {
    private String memberId;
    private String name;

    @Builder
    public EquipmentDTO(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public EquipmentEntity toEquipmentEntity(){
        return EquipmentEntity.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }

}
