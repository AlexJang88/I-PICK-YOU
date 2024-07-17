package com.project.pickyou.entity;

import com.project.pickyou.dto.PointDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "point")
public class PointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id")
    private String memberId;
    private int point;
    private int status;
    @CreationTimestamp
    private Date reg;

    @Builder
    public PointEntity(Long id,String memberId,int point,int status,Date reg) {
        super();
        this.id = id;
        this.memberId = memberId;
        this.point = point;
        this.status = status;
        this.reg = reg;
    }
    public PointDTO toPointDTO(){
        return PointDTO.builder()
                .id(this.id)
                .memberId(this.memberId)
                .point(this.point)
                .status(this.status)
                .reg(this.reg)
                .build();
    }
}
