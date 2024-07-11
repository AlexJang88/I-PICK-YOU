package com.project.pickyou.dto;

import com.project.pickyou.entity.EquipmentEntity;
import com.project.pickyou.entity.JobEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipmentDTO {
    private Long resumeId;
    private String name;

    @Builder
    public EquipmentDTO(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public EquipmentEntity toEquipmentEntity(){
        return EquipmentEntity.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }

}
