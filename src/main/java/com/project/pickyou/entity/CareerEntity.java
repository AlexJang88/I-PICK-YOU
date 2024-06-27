package com.project.pickyou.entity;

import com.project.pickyou.dto.CareerDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "career")
@IdClass(CareerEntity.class)
public class CareerEntity {
    @Id
    @Column(name = "member_id")
    private String memberId;

    @Id
    private String name;

    @Builder
    public CareerEntity(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public CareerDTO toCareerDTO(){
        return CareerDTO.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }
}
