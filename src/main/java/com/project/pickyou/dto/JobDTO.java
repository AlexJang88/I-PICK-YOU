package com.project.pickyou.dto;

import com.project.pickyou.entity.JobEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobDTO {
    private Long resumeId;
    private String name;

    @Builder
    public JobDTO(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public JobEntity toJobEntity(){
        return JobEntity.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }
}
