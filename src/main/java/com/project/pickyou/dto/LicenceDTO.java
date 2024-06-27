package com.project.pickyou.dto;

import com.project.pickyou.entity.JobEntity;
import com.project.pickyou.entity.LicenceEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LicenceDTO {
    private String memberId;
    private String name;

    @Builder
    public LicenceDTO(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public LicenceEntity toLicenceEntity(){
        return LicenceEntity.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }
}
