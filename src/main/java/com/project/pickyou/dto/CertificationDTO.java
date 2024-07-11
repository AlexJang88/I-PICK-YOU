package com.project.pickyou.dto;

import com.project.pickyou.entity.CertificationEntity;
import com.project.pickyou.entity.JobEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CertificationDTO {
    private Long resumeId;
    private String name;

    @Builder
    public CertificationDTO(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public CertificationEntity toCertificationEntity(){
        return CertificationEntity.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }
}
