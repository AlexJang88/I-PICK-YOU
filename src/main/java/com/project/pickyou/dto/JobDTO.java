package com.project.pickyou.dto;

import com.project.pickyou.entity.JobEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobDTO {
    private String memberId;
    private String name;

    @Builder
    public JobDTO(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public JobEntity toJobEntity(){
        return JobEntity.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }
}
