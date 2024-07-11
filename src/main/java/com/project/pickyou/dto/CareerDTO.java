package com.project.pickyou.dto;

import com.project.pickyou.entity.CareerEntity;
import com.project.pickyou.entity.JobEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CareerDTO {
    private Long resumeId;
    private String name;

    @Builder
    public CareerDTO(Long resumeId, String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public CareerEntity toCareerEntity(){
        return CareerEntity.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }
}
