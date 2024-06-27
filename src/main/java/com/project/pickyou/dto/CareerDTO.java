package com.project.pickyou.dto;

import com.project.pickyou.entity.CareerEntity;
import com.project.pickyou.entity.JobEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CareerDTO {
    private String memberId;
    private String name;

    @Builder
    public CareerDTO(String memberId, String name){
        this.memberId=memberId;
        this.name=name;
    }
    public CareerEntity toCareerEntity(){
        return CareerEntity.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }
}
