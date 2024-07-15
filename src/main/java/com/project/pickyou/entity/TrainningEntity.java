package com.project.pickyou.entity;




import com.project.pickyou.dto.TrainningDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "trainning")
@DynamicInsert   //인서트시에 사용
@DynamicUpdate    // 업데이트시에 사용
public class TrainningEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company_id")
    private String companyId;
    private String title;
    private String position;
    private String trainner;
    private String etc;
    private String name;
    private String address;
    private String contact;
    @Column(name = "read_count")
    private int readCount;
    private String content;
    //@CreationTimestamp  <- 만약 디폴트값이 없다면 사용하는것
    @UpdateTimestamp   //업데이트시 필요 <- 디폴트 값으로 들어가있기때문에
    private Date reg;


    @OneToOne
    @JoinColumn(name="company_id",referencedColumnName = "id",insertable = false,updatable = false)
    private MemberEntity memberen;




    @Builder
    public TrainningEntity(Long id, String companyId, String title, String position, String trainner, String etc, String name, String address, String contact, int readCount, String content, Date reg) {
        this.id = id;
        this.companyId = companyId;
        this.title = title;
        this.position = position;
        this.trainner = trainner;
        this.etc = etc;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.readCount = readCount;
        this.content = content;
        this.reg = reg;
    }

    public TrainningDTO toTrainningDTO() {
        return TrainningDTO.builder()
                .id(this.id)
                .companyId(this.companyId)
                .title(this.title)
                .position(this.position)
                .trainner(this.trainner)
                .etc(this.etc)
                .name(this.name)
                .address(this.address)
                .contact(this.contact)
                .readCount(this.readCount)
                .content(this.content)
                .reg(this.reg)
                .build();
    }
}
