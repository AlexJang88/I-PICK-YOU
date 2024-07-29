package com.project.pickyou.entity;

import com.project.pickyou.dto.PickDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
@Table(name = "pick")
@IdClass(PickID.class)
public class PickEntity {

    @Id
    private String picker;
    @Id
    private String target;

    @OneToOne
    @JoinColumn(name = "target",referencedColumnName = "id",insertable = false,updatable = false)
    private MemberEntity member;

    @Builder
    public PickEntity(String picker, String target) {
        super();
        this.picker = picker;
        this.target = target;
    }
        public PickDTO toPickDTO() {
            return PickDTO.builder()
                    .picker(this.picker)
                .target(this.target)
                 .build();
        }




}
