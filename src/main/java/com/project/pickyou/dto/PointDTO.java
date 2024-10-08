package com.project.pickyou.dto;

import com.project.pickyou.entity.PointEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class PointDTO {
    private Long id;
    private String memberId;
    private int point;
    private int status;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private Date reg;

    @Builder
    public PointDTO(Long id,String memberId,int point,int status,Date reg){
        super();
        this.id=id;
        this.memberId=memberId;
        this.point=point;
        this.status=status;
        this.reg=reg;
    }
    public PointEntity toPointEntity(){
        return PointEntity.builder()
                .id(this.id)
                .memberId(this.memberId)
                .point(this.point)
                .status(this.status)
                .reg(this.reg)
                .build();
    }


}
