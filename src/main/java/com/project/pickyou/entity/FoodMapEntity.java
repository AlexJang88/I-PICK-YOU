package com.project.pickyou.entity;

import com.project.pickyou.dto.FoodMapDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Data
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "food_map")
public class FoodMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id")
    private String memberId;
    private String title;
    private String content;
    private String map;
    private int ref;
    private int reply;
    @Column(name = "read_count")
    private int readCount;
    private Date reg;
    private int status;

    @Builder
    public FoodMapEntity(Long id, String memberId, String title, String content, String map, int ref, int reply, int readCount, Date reg, int status) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.map = map;
        this.ref = ref;
        this.reply = reply;
        this.readCount = readCount;
        this.reg = reg;
        this.status = status;
    }

    public FoodMapDTO toFood_MapDTO() {
        return FoodMapDTO.builder()
                .id(this.id)
                .memberId(this.memberId)
                .title(this.title)
                .content(this.content)
                .map(this.map)
                .ref(this.ref)
                .reply(this.reply)
                .readCount(this.readCount)
                .reg(this.reg)
                .status(this.status)
                .build();
    }
}
