package com.project.pickyou.entity;

import com.project.pickyou.dto.JobDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NoArgsConstructor
@Data
@Table(name = "job")
@DynamicInsert
@DynamicUpdate
@IdClass(JobID.class)
public class JobEntity {
    @Id
    @Column(name = "resume_id")
    private Long resumeId;

    @Id
    private String name;

    @Builder
    public JobEntity(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public JobDTO toJobDTO(){
        return JobDTO.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }


}
