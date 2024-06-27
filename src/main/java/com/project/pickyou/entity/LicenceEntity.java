package com.project.pickyou.entity;

import com.project.pickyou.dto.JobDTO;
import com.project.pickyou.dto.LicenceDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "licence")
@IdClass(LicenceID.class)
public class LicenceEntity {
    @Id
    @Column(name = "member_id")
    private String memberId;

    @Id
    private String name;

    @Builder
    public LicenceEntity(String memberId,String name){
        this.memberId=memberId;
        this.name=name;
    }
    public LicenceDTO toLicenceDTO(){
        return LicenceDTO.builder()
                .memberId(this.memberId)
                .name(this.name)
                .build();
    }


}
