package com.project.pickyou.entity;

import com.project.pickyou.dto.AlarmDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@Entity
@Table(name = "alarm")
public class AlarmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id")
    private String memberId;
    private String content;
    @Column(name = "reader_id")
    private String readerId;
    private int status;
    private Date reg;

    @Builder
    public AlarmEntity(Long id,String memberId,String content,String readerId,int status,Date reg){
        this.id=id;
        this.memberId=memberId;
        this.content=content;
        this.readerId=readerId;
        this.status=status;
        this.reg=reg;
    }
    public AlarmDTO toAlarmDTO(){
        return AlarmDTO.builder()
                .id(this.id)
                .memberId(this.memberId)
                .content(this.content)
                .readerId(this.readerId)
                .status(this.status)
                .reg(this.reg)
                .build();
    }
}
