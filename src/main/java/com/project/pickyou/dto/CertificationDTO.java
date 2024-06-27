package com.project.pickyou.dto;

import com.project.pickyou.entity.CertificationEntity;
import com.project.pickyou.entity.JobEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CertificationDTO {
    private String memberId;
    private String name;

    @Builder
    public CertificationDTO(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public CertificationEntity toCertificationEntity(){
        return CertificationEntity.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }
}
