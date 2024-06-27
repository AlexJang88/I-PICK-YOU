package com.project.pickyou.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class PaymentDTO {
    private Long id;
    private String memberId;
    private int money;
    private int point;
    private String title;
    private int pointHistory;
    private Date reg;

    @Builder
    public PaymentDTO(Long id,String memberId,int money,int point,String title,int pointHistory,Date reg){
        super();
        this.id=id;
        this.memberId=memberId;
        this.money=money;
        this.point=point;
        this.title=title;
        this.pointHistory=pointHistory;
        this.reg=reg;
    }
}
