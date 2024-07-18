package com.project.pickyou.entity;

import com.project.pickyou.dto.CareerDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "career")
@IdClass(CareerID.class)
public class CareerEntity {
    @Id
    @Column(name = "resume_id")
    private Long resumeId;

    @Id
    private String name;

    @OneToOne
    @MapsId
    @JoinColumn(name = "resume_id")
    private ResumeEntity resume;


    @Builder
    public CareerEntity(Long resumeId,String name){
        this.resumeId=resumeId;
        this.name=name;
    }
    public CareerDTO toCareerDTO(){
        return CareerDTO.builder()
                .resumeId(this.resumeId)
                .name(this.name)
                .build();
    }
}
