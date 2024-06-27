package com.project.pickyou.dto;

import com.project.pickyou.entity.QaEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class QaDTO {
    private Long id;
    private String title;
    private String content;
    private String memberId;
    private int ref;
    private Date reg;

    @Builder
    public QaDTO(Long id, String title, String content, String memberId, int ref, Date reg) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.ref = ref;
        this.reg = reg;
    }

    public QaEntity toQaEntity() {
        return QaEntity.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .memberId(this.memberId)
                .ref(this.ref)
                .reg(this.reg)
                .build();
    }
}
