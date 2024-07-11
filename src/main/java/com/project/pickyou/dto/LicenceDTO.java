package com.project.pickyou.dto;

import com.project.pickyou.entity.JobEntity;
import com.project.pickyou.entity.LicenceEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LicenceDTO {
    private Long resumeId;
    private String name;

    @Builder
    public LicenceDTO(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public LicenceEntity toLicenceEntity(){
        return LicenceEntity.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }
}
