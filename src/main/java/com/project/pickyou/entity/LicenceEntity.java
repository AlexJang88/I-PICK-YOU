package com.project.pickyou.entity;

import com.project.pickyou.dto.JobDTO;
import com.project.pickyou.dto.LicenceDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NoArgsConstructor
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "licence")
@IdClass(LicenceID.class)
public class LicenceEntity {
    @Id
    @Column(name = "resume_id")
    private Long resumeId;

    @Id
    private String name;

    @Builder
    public LicenceEntity(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public LicenceDTO toLicenceDTO(){
        return LicenceDTO.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }


}
