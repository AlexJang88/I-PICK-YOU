package com.project.pickyou.dto;

import com.project.pickyou.entity.FoodMapEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class FoodMapDTO {
    private Long id;
    private String memberId;
    private String title;
    private String content;
    private String map;
    private int ref;
    private int reply;
    private int readCount;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private Date reg;
    private int status;

    @Builder
    public FoodMapDTO(Long id, String memberId, String title, String content, String map, int ref, int reply, int readCount, Date reg, int status) {
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

    public FoodMapEntity toFood_MapEntity() {
        return FoodMapEntity.builder()
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
