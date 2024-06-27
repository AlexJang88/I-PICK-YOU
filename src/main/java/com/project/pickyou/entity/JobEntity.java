package com.project.pickyou.entity;

import com.project.pickyou.dto.JobDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "job")
@IdClass(JobID.class)
public class JobEntity {
    @Id
    @Column(name = "member_id")
    private String memberId;

    @Id
    private String name;

    @Builder
    public JobEntity(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public JobDTO toJobDTO(){
        return JobDTO.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }


}
