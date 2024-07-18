package com.project.pickyou.entity;

import com.project.pickyou.dto.PaymentDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id")
    private String memberId;
    private int money;
    private int point;
    private String title;
    @Column(name = "point_history")
    private int pointHistory;
    private Date reg;

    @Builder
    public PaymentEntity(Long id,String memberId,int money,int point,String title,int pointHistory,Date reg){
        super();
        this.id=id;
        this.memberId=memberId;
        this.money=money;
        this.point=point;
        this.title=title;
        this.pointHistory=pointHistory;
        this.reg=reg;
    }

    public PaymentDTO paymentDTO() {
        return PaymentDTO.builder()
                .id(this.id)
                .memberId(this.memberId)
                .money(this.money)
                .point(this.point)
                .title(this.title)
                .pointHistory(pointHistory)
                .reg(this.reg)
                .build();
    }
}
