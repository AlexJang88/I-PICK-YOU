package com.project.pickyou.entity;

import com.project.pickyou.dto.AgencyDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name ="agency")
@DynamicInsert
@DynamicUpdate
public class AgencyEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "company_id")
        private String companyId;
        @Column(name = "company_name")
        private String companyName;
        private String business;
        private String contact;
        private String address;
        private String content;
        @Column(name = "read_count")
        private int readCount;
        @UpdateTimestamp
        private Date reg;


    @OneToOne
    @JoinColumn(name="company_id",referencedColumnName = "id",insertable = false,updatable = false)
    private MemberEntity memberen;


        @Builder
        public AgencyEntity(Long id, String companyId, String companyName, String business,
                         String contact, String address, String content, int readCount, Date reg){
            this.id = id;
            this.companyId = companyId;
            this.companyName =companyName;
            this.contact = contact;
            this.business = business;
            this.address = address;
            this.content = content;
            this.readCount = readCount;
            this.reg = reg;

        }

        public AgencyDTO toAgencyDTO(){
            return AgencyDTO.builder()
                    .id(this.id)
                    .companyId(this.companyId)
                    .companyName(this.companyName)
                    .contact(this.contact)
                    .business(this.business)
                    .address(this.address)
                    .content(this.content)
                    .readCount(this.readCount)
                    .reg(this.reg)
                    .build();
        }



}
