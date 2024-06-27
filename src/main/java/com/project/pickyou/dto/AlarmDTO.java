package com.project.pickyou.dto;

import com.project.pickyou.entity.AlarmEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AlarmDTO {
    private Long id;
    private String memberId;
    private String content;
    private String readerId;
    private int status;
    private Date reg;

    @Builder
    public AlarmDTO(Long id,String memberId,String content,String readerId,int status,Date reg){
        this.id=id;
        this.memberId=memberId;
        this.content=content;
        this.readerId=readerId;
        this.status=status;
        this.reg=reg;
    }
    public AlarmEntity toAlarmEntity(){
        return AlarmEntity.builder()
                .id(this.id)
                .memberId(this.memberId)
                .content(this.content)
                .readerId(this.readerId)
                .status(this.status)
                .reg(this.reg)
                .build();
    }
}
